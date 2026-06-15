package com.openclassroom.eventorias.core.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.openclassroom.eventorias.core.data.mapping.toDomain
import com.openclassroom.eventorias.core.data.model.EventDto
import com.openclassroom.eventorias.core.domain.model.Event
import com.openclassroom.eventorias.core.domain.repository.EventRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FirebaseEventRepository @Inject constructor(private val firestore: FirebaseFirestore) :
    EventRepository {
    override fun getListEvent(): Flow<List<Event>> {
        val today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        return firestore.collection("events")
            .whereGreaterThanOrEqualTo("dateTime", today)
            .orderBy("dateTime", Query.Direction.ASCENDING)
            .snapshots()
            .map { snapshots ->
                snapshots.documents.mapNotNull { documentSnapshot ->
                    documentSnapshot.toObject(EventDto::class.java)?.toDomain()
                }
            }
    }

    override fun getEventById(id: String): Flow<Event?> {
        return firestore.collection("events")
            .document(id)
            .snapshots()
            .map { documentSnapshot ->
                documentSnapshot.toObject(EventDto::class.java)?.toDomain()
            }
    }

    override suspend fun setParticipationStatus(
        newStatus: Boolean,
        userId: String,
        eventId: String
    ): Result<Unit> = runCatching {
        if (newStatus) {
            val data = mapOf("participantId" to userId)
            firestore.collection("events").document(eventId)
                .collection("participants").document(userId).set(data).await()
        } else {
            firestore.collection("events").document(eventId)
                .collection("participants").document(userId).delete().await()
        }
    }

    override fun getParticipantsList(eventId: String): Flow<List<String>> {
        return firestore.collection("events").document(eventId).collection("participants")
            .snapshots().map { querySnapshots ->
                querySnapshots.documents.map { documentSnapshot ->
                    documentSnapshot.id
                }
            }
    }

}
