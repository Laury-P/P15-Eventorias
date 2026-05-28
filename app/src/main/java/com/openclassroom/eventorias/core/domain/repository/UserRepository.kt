package com.openclassroom.eventorias.core.domain.repository

import com.openclassroom.eventorias.core.domain.model.User

interface UserRepository {
    suspend fun getCurrentUser (userId : String) : User?
}