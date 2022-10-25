package com.seif.cleanarchitecturenoteappwithfirebase.domain.usecase

import com.seif.cleanarchitecturenoteappwithfirebase.domain.model.Note
import com.seif.cleanarchitecturenoteappwithfirebase.domain.repository.NoteRepository
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class AddNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(note: Note) = flow {
        when (val result = validNote(note)) {
            is Resource.Error -> emit(result)
            is Resource.Success -> noteRepository.addNote(note).collect{
                emit(it)
            }
        }

    }

    private fun validNote(note: Note): Resource<String, String> {
        return if (note.title.length < 5) {
            Resource.Error("title is too short min char = 5")
        } else if (note.title.length > 40) {
            Resource.Error("title is to0 long max char = 40")
        } else if (note.description.length < 20) {
            Resource.Error("title is too short min char = 20")

        } else if (note.description.length > 100) { // to long ""
            Resource.Error("title is to0 long max char = 100")
        } else {
            Resource.Success("valid note")
        }
    }
}