package com.seif.cleanarchitecturenoteappwithfirebase.presentation.note_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seif.cleanarchitecturenoteappwithfirebase.domain.model.Note
import com.seif.cleanarchitecturenoteappwithfirebase.domain.usecase.GetNotesUseCase
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase
) : ViewModel() {
    private val TAG = "NoteListViewModel"

    private val _state = MutableStateFlow<NoteListFragmentState>(NoteListFragmentState.Init)
    val state: StateFlow<NoteListFragmentState> get() = _state

    init {
        getNotes()
    }

    private fun setLoading(isLoading: Boolean) {
        _state.value = NoteListFragmentState.IsLoading(isLoading)
    }

    private fun showError(message: String) {
        _state.value = NoteListFragmentState.ShowError(message)
    }

    private fun getNotes() {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            getNotesUseCase()
                .collect { result ->
                when (result) {
                    is Resource.Success -> {
                        withContext(Dispatchers.Main) {
                            setLoading(false)
                            // Log.d(TAG, "getNotes: ${result.data}")
                            _state.value = NoteListFragmentState.Notes(result.data)
                        }
                    }
                    is Resource.Error -> {
                        withContext(Dispatchers.Main) {
                            setLoading(false)
                            showError(result.message)
                        }
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
    data class Notes(val notes:List<Note>) : NoteListFragmentState()
}