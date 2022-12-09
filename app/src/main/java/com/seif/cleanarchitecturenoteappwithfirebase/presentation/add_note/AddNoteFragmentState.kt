package com.seif.cleanarchitecturenoteappwithfirebase.presentation.add_note

import android.net.Uri

sealed class AddNoteFragmentState {
    object Init : AddNoteFragmentState()
    data class IsLoading(val isLoading: Boolean) : AddNoteFragmentState()
    data class ShowError(val message: String) : AddNoteFragmentState()
    data class NoteId(val noteId: String) : AddNoteFragmentState()
    data class ImageUploaded(val uri: Uri) : AddNoteFragmentState()
    data class ImagesUploaded(val imagesUri: List<Uri>) : AddNoteFragmentState()
}
