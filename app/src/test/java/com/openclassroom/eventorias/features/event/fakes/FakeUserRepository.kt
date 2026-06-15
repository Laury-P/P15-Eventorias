package com.openclassroom.eventorias.features.event.fakes

import com.openclassroom.eventorias.core.domain.model.User
import com.openclassroom.eventorias.core.domain.repository.UserRepository

class FakeUserRepository : UserRepository{
    private val users = mutableMapOf<String, User>()

    override suspend fun getCurrentUser(userId: String): User? {
        return users[userId]
    }

    override suspend fun addUser(user: User): Result<Unit> {
        users[user.id] = user
        return Result.success(Unit)
    }
}