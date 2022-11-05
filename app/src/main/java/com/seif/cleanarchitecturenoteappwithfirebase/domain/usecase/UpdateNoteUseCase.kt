package com.seif.cleanarchitecturenoteappwithfirebase.domain.usecase

import com.seif.cleanarchitecturenoteappwithfirebase.domain.model.Note
import com.seif.cleanarchitecturenoteappwithfirebase.domain.repository.NoteRepository
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Resource
import com.seif.cleanarchitecturenoteappwithfirebase.utils.validNote
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(updatedNote: Note) = flow {
        when (val result = updatedNote.validNote()) {
            is Resource.Error -> emit(result)
            is Resource.Success -> noteRepository.updateNote(updatedNote).collect {
                emit(it)
            }
        }
    }
}
