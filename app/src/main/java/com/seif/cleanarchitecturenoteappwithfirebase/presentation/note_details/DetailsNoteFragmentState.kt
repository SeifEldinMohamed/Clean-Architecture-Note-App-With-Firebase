package com.seif.cleanarchitecturenoteappwithfirebase.presentation.note_details

sealed class DetailsNoteFragmentState {
    object Init : DetailsNoteFragmentState()
    data class IsLoading(val isLoading: Boolean) : DetailsNoteFragmentState()
    data class ShowError(val message: String) : DetailsNoteFragmentState()
    data class NoteId(val noteId:String) : DetailsNoteFragmentState()
}