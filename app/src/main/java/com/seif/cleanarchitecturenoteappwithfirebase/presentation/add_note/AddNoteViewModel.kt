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
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val addNoteUseCase: AddNoteUseCase
): ViewModel() {

    private val _state = MutableStateFlow<AddNoteFragmentState>(AddNoteFragmentState.Init)
    val state: StateFlow<AddNoteFragmentState> = _state

    private val _noteId = MutableStateFlow<String>("")
    val noteId: StateFlow<String> = _noteId

    private fun showError(message:String) {
        _state.value = AddNoteFragmentState.ShowError(message)
    }
    private fun setLoading(isLoading: Boolean) {
        when(isLoading){
            true -> _state.value = AddNoteFragmentState.IsLoading(true)
            false -> _state.value = AddNoteFragmentState.IsLoading(false)
        }
    }

    fun addNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            addNoteUseCase(note)
                .onStart { setLoading(true) }
                .collect {
                when(it) {
                    is Resource.Error -> {
                        setLoading(false)
                        showError(it.message)
                    }
                    is Resource.Success -> {
                        setLoading(false)
                        _noteId.value = it.data
                    }
                }
            }
        }
    }
}