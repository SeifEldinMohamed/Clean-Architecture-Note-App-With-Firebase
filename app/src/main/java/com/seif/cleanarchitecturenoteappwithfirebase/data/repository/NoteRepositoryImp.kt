package com.seif.cleanarchitecturenoteappwithfirebase.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.seif.cleanarchitecturenoteappwithfirebase.data.remote.dto.NoteDto
import com.seif.cleanarchitecturenoteappwithfirebase.domain.repository.NoteRepository
import javax.inject.Inject

class NoteRepositoryImp @Inject constructor(
    private val firestore: FirebaseFirestore
) : NoteRepository {
    override fun getNotes(): List<NoteDto> {
        return emptyList()
    }
}