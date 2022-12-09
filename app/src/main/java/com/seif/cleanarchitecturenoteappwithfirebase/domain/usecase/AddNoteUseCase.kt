package com.seif.cleanarchitecturenoteappwithfirebase.domain.usecase

import com.seif.cleanarchitecturenoteappwithfirebase.domain.model.Note
import com.seif.cleanarchitecturenoteappwithfirebase.domain.repository.NoteRepository
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Resource
import com.seif.cleanarchitecturenoteappwithfirebase.utils.validNote
import javax.inject.Inject

class AddNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(note: Note): Resource<String, String> {
        return when (val result = note.validNote()) {
            is Resource.Error -> result
            is Resource.Success -> noteRepository.addNote(note)
        }
    }
}
