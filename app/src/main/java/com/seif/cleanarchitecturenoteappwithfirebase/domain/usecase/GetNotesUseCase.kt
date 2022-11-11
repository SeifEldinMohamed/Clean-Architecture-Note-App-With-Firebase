package com.seif.cleanarchitecturenoteappwithfirebase.domain.usecase

import com.seif.cleanarchitecturenoteappwithfirebase.domain.model.Note
import com.seif.cleanarchitecturenoteappwithfirebase.domain.repository.NoteRepository
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(): Flow<Resource<List<Note>, String>> {
        return noteRepository.getNotes()
    }

}
