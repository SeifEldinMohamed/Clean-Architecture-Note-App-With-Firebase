package com.seif.cleanarchitecturenoteappwithfirebase.domain.repository

import android.net.Uri
import com.seif.cleanarchitecturenoteappwithfirebase.domain.model.Note
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Resource
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getNotes(userId: String): Flow<Resource<List<Note>, String>>
    suspend fun addNote(note: Note): Resource<String, String>
    fun updateNote(note: Note): Flow<Resource<String, String>>
    fun deleteNote(note: Note): Flow<Resource<String, String>>
    suspend fun uploadSingleImage(imageUri: Uri): Flow<Resource<Uri, String>>
    suspend fun uploadMultipleImages(imagesUri: List<Uri>): Resource<List<Uri>, String>
}
