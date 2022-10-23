package com.seif.cleanarchitecturenoteappwithfirebase.data.remote.dto

import com.google.firebase.firestore.ServerTimestamp

data class NoteDto(
    val id:String,
    val title:String,
    @ServerTimestamp // it will store timestamp on the fireSztore in the document
    val date:String
)
