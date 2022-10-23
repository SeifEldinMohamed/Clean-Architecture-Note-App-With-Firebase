package com.seif.cleanarchitecturenoteappwithfirebase.presentation.note_list

import androidx.lifecycle.ViewModel
import com.seif.cleanarchitecturenoteappwithfirebase.data.remote.dto.NoteDto
import com.seif.cleanarchitecturenoteappwithfirebase.domain.usecase.GetNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase
): ViewModel() {

    fun getNotesList(): List<NoteDto> {
        return getNotesUseCase()
    }
}