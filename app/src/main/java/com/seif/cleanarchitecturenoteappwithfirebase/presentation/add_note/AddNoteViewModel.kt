package com.seif.cleanarchitecturenoteappwithfirebase.presentation.add_note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seif.cleanarchitecturenoteappwithfirebase.domain.model.Note
import com.seif.cleanarchitecturenoteappwithfirebase.domain.usecase.AddNoteUseCase
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val addNoteUseCase: AddNoteUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<AddNoteFragmentState>(AddNoteFragmentState.Init)
    val state: StateFlow<AddNoteFragmentState> = _state

    private fun showError(message: String) {
        _state.value = AddNoteFragmentState.ShowError(message)
    }

    private fun setLoading(isLoading: Boolean) {
        when (isLoading) {
            true -> _state.value = AddNoteFragmentState.IsLoading(true)
            false -> _state.value = AddNoteFragmentState.IsLoading(false)
        }
    }

    fun addNote(note: Note) {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            addNoteUseCase(note)
                .collect {
                    when (it) {
                        is Resource.Error -> {
                            withContext(Dispatchers.Main) {
                                setLoading(false)
                                showError(it.message)
                            }
                        }
                        is Resource.Success -> {
                            withContext(Dispatchers.Main) {
                                setLoading(false)
                                _state.value = AddNoteFragmentState.NoteId(it.data)
                            }
                        }
                    }
                }
        }
    }
}
