package com.seif.cleanarchitecturenoteappwithfirebase.data.remote.dto

import androidx.annotation.Keep
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

@Keep
data class NoteDto(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val description: String = "",
    @ServerTimestamp // it will store timestamp on the fireStore in the document
    val date: Date = Date(),
    val images: List<String> = emptyList()
)
