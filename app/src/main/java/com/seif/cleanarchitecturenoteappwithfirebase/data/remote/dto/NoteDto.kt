package com.seif.cleanarchitecturenoteappwithfirebase.data.remote.dto

import androidx.annotation.Keep
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

@Keep
data class NoteDto(
    val id:String = "",
    val title:String = "",
    val description:String = "",
    @ServerTimestamp // it will store timestamp on the fireStore in the document
    val date:Date = Date()
)
