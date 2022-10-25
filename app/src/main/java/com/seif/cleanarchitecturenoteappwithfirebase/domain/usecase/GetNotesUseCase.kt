package com.seif.cleanarchitecturenoteappwithfirebase.domain.usecase

import androidx.paging.Pager
import com.google.firebase.firestore.QuerySnapshot
import com.seif.cleanarchitecturenoteappwithfirebase.data.remote.dto.NoteDto
import com.seif.cleanarchitecturenoteappwithfirebase.domain.model.Note
import com.seif.cleanarchitecturenoteappwithfirebase.domain.repository.NoteRepository
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
     operator fun invoke(): Flow<Resource<Pager<QuerySnapshot, Note>, String>> {
        return noteRepository.getNotes()
    }
}