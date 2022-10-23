package com.seif.cleanarchitecturenoteappwithfirebase.domain.repository

import com.seif.cleanarchitecturenoteappwithfirebase.data.remote.dto.NoteDto

interface NoteRepository {
    fun getNotes(): List<NoteDto>
}