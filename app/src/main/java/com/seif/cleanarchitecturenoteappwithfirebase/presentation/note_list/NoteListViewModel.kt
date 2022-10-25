package com.seif.cleanarchitecturenoteappwithfirebase.presentation.note_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seif.cleanarchitecturenoteappwithfirebase.domain.model.Note
import com.seif.cleanarchitecturenoteappwithfirebase.domain.usecase.GetNotesUseCase
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase
) : ViewModel() {
    private val TAG = "NoteListViewModel"

    private val _state = MutableStateFlow<NoteListFragmentState>(NoteListFragmentState.Init)
    val state: StateFlow<NoteListFragmentState> get() = _state

    private val _notes = MutableStateFlow<List<Note>>(mutableListOf())
    val notes: StateFlow<List<Note>> = _notes

//    init {
//        getNotes()
//    }

    private fun setLoading(isLoading: Boolean) {
        _state.value = NoteListFragmentState.IsLoading(isLoading)
    }

    private fun showError(message: String) {
        _state.value = NoteListFragmentState.ShowError(message)
    }

    fun getNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            getNotesUseCase()
                .onStart { setLoading(true) } // to simulate network call
                .collect { result ->
                when (result) {
                    is Resource.Success -> {
                        setLoading(false)
                        Log.d(TAG, "getNotes: ${result.data}")
                        _notes.value = result.data
                    }
                    is Resource.Error -> {
                        setLoading(false)
                        showError(result.message)
                    }
                }
            }
        }
    }
}

sealed class NoteListFragmentState {
    object Init : NoteListFragmentState()
    data class IsLoading(val isLoading: Boolean) : NoteListFragmentState()
    data class ShowToast(val message: String) : NoteListFragmentState()
    data class ShowError(val message: String) : NoteListFragmentState()
}