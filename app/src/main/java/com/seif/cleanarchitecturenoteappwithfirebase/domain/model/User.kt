package com.seif.cleanarchitecturenoteappwithfirebase.domain.model

data class User(
    val name: String,
    val email: String,
    val password: String,
    val subscribed: Boolean
)
