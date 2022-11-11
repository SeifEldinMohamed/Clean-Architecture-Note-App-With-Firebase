package com.seif.cleanarchitecturenoteappwithfirebase.domain.repository

import com.seif.cleanarchitecturenoteappwithfirebase.domain.model.Note
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Resource
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getNotes(): Flow<Resource<List<Note>, String>>
    fun addNote(note: Note): Flow<Resource<String, String>>
    fun updateNote(note: Note): Flow<Resource<String, String>>
    fun deleteNote(note: Note): Flow<Resource<String, String>>
}
