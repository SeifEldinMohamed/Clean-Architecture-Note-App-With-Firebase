package com.seif.cleanarchitecturenoteappwithfirebase.data.repository

import com.seif.cleanarchitecturenoteappwithfirebase.data.remote.dto.NoteDto
import com.seif.cleanarchitecturenoteappwithfirebase.domain.repository.NoteRepository

class NoteRepositoryImp(): NoteRepository {
    override fun getNotes(): List<NoteDto> {
        return emptyList()
    }
}