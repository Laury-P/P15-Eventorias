package com.openclassroom.eventorias.core.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.openclassroom.eventorias.core.domain.model.User
import com.openclassroom.eventorias.core.domain.repository.UserRepository
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class FirebaseUserRepository @Inject constructor(private val firestore: FirebaseFirestore) :
    UserRepository {

    override suspend fun getCurrentUser(userId: String): User? {
        val document = firestore.collection("users").document(userId).get().await()
        return document.toObject(User::class.java)
    }

    override suspend fun addUser(user: User): Result<Unit> = runCatching{
        firestore.collection("users").document(user.id).set(user).await()
    }

}