package com.seif.cleanarchitecturenoteappwithfirebase.domain.usecase

import com.seif.cleanarchitecturenoteappwithfirebase.data.remote.dto.NoteDto
import com.seif.cleanarchitecturenoteappwithfirebase.domain.repository.NoteRepository

class GetNotesUseCase(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(): List<NoteDto> {
        return noteRepository.getNotes()
    }
}