package com.seif.cleanarchitecturenoteappwithfirebase.domain.repository

import com.seif.cleanarchitecturenoteappwithfirebase.domain.model.User
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun registerUser(user: User): Flow<Resource<User, String>>
}