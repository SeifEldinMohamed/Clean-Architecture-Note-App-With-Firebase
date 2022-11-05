package com.seif.cleanarchitecturenoteappwithfirebase.presentation.note_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seif.cleanarchitecturenoteappwithfirebase.domain.model.Note
import com.seif.cleanarchitecturenoteappwithfirebase.domain.usecase.UpdateNoteUseCase
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NoteDetailsViewModel @Inject constructor(
    private val updateNoteUseCase: UpdateNoteUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<DetailsNoteFragmentState>(DetailsNoteFragmentState.Init)
    val state: StateFlow<DetailsNoteFragmentState> = _state

    private fun showError(message: String) {
        _state.value = DetailsNoteFragmentState.ShowError(message)
    }

    private fun setLoading(isLoading: Boolean) {
        when (isLoading) {
            true -> _state.value = DetailsNoteFragmentState.IsLoading(true)
            false -> _state.value = DetailsNoteFragmentState.IsLoading(false)
        }
    }

    fun updateNote(note: Note) {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            updateNoteUseCase(note)
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
                                _state.value = DetailsNoteFragmentState.NoteId(it.data)
                            }
                        }
                    }
                }
        }
    }
}
