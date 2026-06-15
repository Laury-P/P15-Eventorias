package com.openclassroom.eventorias.features.event.fakes

import com.openclassroom.eventorias.core.domain.repository.AuthRepository

class FakeAuthRepository : AuthRepository {
    var currentUserId : String? = null

    override fun getUserId(): String? = currentUserId
}