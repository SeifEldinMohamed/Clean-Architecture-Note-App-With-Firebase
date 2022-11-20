package com.seif.cleanarchitecturenoteappwithfirebase.domain.repository

import com.seif.cleanarchitecturenoteappwithfirebase.domain.model.User
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun registerUser(user: User): Flow<Resource<User, String>>
    fun loginInUser(email: String, password: String): Flow<Resource<String, String>>
    fun forgetPassword(email: String): Flow<Resource<String, String>>
    suspend fun logOut(): Resource<String, String>
    fun getSession(): User?
}