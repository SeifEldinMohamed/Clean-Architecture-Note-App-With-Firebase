package com.seif.cleanarchitecturenoteappwithfirebase.domain.model

import androidx.annotation.Keep

@Keep
data class User(
    val name: String,
    val email: String,
    val password: String,
    val subscribed: Boolean
)
