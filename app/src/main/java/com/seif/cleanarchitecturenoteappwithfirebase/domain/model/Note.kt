package com.seif.cleanarchitecturenoteappwithfirebase.domain.model

import androidx.annotation.Keep
import java.util.*

@Keep
data class Note(
    var id:String,
    val title:String,
    val description: String,
    val date:Date
)
