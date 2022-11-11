package com.seif.cleanarchitecturenoteappwithfirebase.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class User(
    var id: String,
    val username: String,
    val email: String,
    val password: String,
    val subscribed: Boolean
) : Parcelable
