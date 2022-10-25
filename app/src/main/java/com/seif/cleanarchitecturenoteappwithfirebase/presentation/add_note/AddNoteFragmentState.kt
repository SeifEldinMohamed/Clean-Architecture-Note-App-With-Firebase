package com.seif.cleanarchitecturenoteappwithfirebase.presentation.add_note

sealed class AddNoteFragmentState {
    object Init : AddNoteFragmentState()
    data class IsLoading(val isLoading: Boolean) : AddNoteFragmentState()
    data class ShowError(val message: String) : AddNoteFragmentState()
    data class NoteId(val noteId:String) : AddNoteFragmentState()
}