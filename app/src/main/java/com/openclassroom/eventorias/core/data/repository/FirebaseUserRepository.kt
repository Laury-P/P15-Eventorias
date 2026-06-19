package com.openclassroom.eventorias.core.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.openclassroom.eventorias.core.domain.model.User
import com.openclassroom.eventorias.core.domain.repository.UserRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class FirebaseUserRepository @Inject constructor(private val firestore: FirebaseFirestore) :
    UserRepository {

    override suspend fun getUserById(userId: String): User? {
        val document = firestore.collection("users").document(userId).get().await()
        return document.toObject(User::class.java)
    }

    override suspend fun addUser(user: User): Result<Unit> = runCatching{
        firestore.collection("users").document(user.id).set(user).await()
    }

    override fun observeUserById(userId: String): Flow<User?> {
        return firestore.collection("users")
            .document(userId)
            .snapshots()
            .map{documentSnapshot ->
                documentSnapshot.toObject(User::class.java)
            }
    }

}