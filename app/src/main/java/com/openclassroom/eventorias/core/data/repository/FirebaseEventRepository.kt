package com.openclassroom.eventorias.core.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.openclassroom.eventorias.core.data.mapping.toDomain
import com.openclassroom.eventorias.core.data.model.EventDto
import com.openclassroom.eventorias.core.domain.model.Event
import com.openclassroom.eventorias.core.domain.repository.EventRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FirebaseEventRepository @Inject constructor(private val firestore: FirebaseFirestore) :
    EventRepository {
    override fun getListEvent(): Flow<List<Event>> = firestore.collection("events")
        .snapshots()
        .map { snapshots ->
            snapshots.documents.mapNotNull { documentSnapshot ->
                documentSnapshot.toObject(EventDto::class.java)?.toDomain()
            }
        }
}
