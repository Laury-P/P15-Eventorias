package com.openclassroom.eventorias.core.data.repository

import com.firebase.ui.auth.FirebaseAuthUI
import com.openclassroom.eventorias.core.domain.repository.AuthRepository
import jakarta.inject.Inject

class FirebaseAuthRepository @Inject constructor(private val authUI: FirebaseAuthUI): AuthRepository {
    override fun getUserId(): String? {
        return authUI.auth.currentUser?.uid
    }
}