package com.seif.cleanarchitecturenoteappwithfirebase.data.remote.dto

import androidx.annotation.Keep

@Keep
data class UserDto(
    val id: String,
    val username: String,
    val email: String,
    val password: String,
    val subscribed: Boolean
)
