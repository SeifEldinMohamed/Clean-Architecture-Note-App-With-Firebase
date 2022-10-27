package com.seif.cleanarchitecturenoteappwithfirebase.domain.repository

import androidx.paging.Pager
import com.google.firebase.firestore.QuerySnapshot
import com.seif.cleanarchitecturenoteappwithfirebase.data.remote.dto.NoteDto
import com.seif.cleanarchitecturenoteappwithfirebase.domain.model.Note
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Resource
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getNotes(): Flow<Resource<Pager<QuerySnapshot, Note>, String>>

    fun addNote(note:Note): Flow<Resource<String, String>>
}