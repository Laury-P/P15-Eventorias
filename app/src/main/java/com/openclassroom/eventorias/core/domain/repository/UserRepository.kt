package com.openclassroom.eventorias.core.domain.repository

import com.openclassroom.eventorias.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUserById (userId : String) : User?
    suspend fun addUser(user: User) : Result<Unit>
    fun observeUserById(userId : String) : Flow<User?>
}