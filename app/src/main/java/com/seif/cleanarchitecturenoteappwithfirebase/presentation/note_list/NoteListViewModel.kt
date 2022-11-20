package com.seif.cleanarchitecturenoteappwithfirebase.presentation.note_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.seif.cleanarchitecturenoteappwithfirebase.domain.model.Note
import com.seif.cleanarchitecturenoteappwithfirebase.domain.usecase.DeleteNoteUseCase
import com.seif.cleanarchitecturenoteappwithfirebase.domain.usecase.GetFirebaseCurrentUserUseCase
import com.seif.cleanarchitecturenoteappwithfirebase.domain.usecase.GetNotesUseCase
import com.seif.cleanarchitecturenoteappwithfirebase.domain.usecase.LogoutUseCase
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
    private val getNotesUseCase: GetNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getFirebaseCurrentUserUseCase: GetFirebaseCurrentUserUseCase
) : ViewModel() {
    private val TAG = "NoteListViewModel"

    private val _state = MutableStateFlow<NoteListFragmentState>(NoteListFragmentState.Init)
    val state: StateFlow<NoteListFragmentState> get() = _state

    private fun setLoading(isLoading: Boolean) {
        _state.value = NoteListFragmentState.IsLoading(isLoading)
    }

    private fun showError(message: String) {
        _state.value = NoteListFragmentState.ShowError(message)
    }

    fun getNotes(userId: String) {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            getNotesUseCase(userId)
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

    fun deleteNote(note: Note) {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            deleteNoteUseCase(note)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            withContext(Dispatchers.Main) {
                                setLoading(false)
                                Log.d(TAG, "deleteNote: ${result.data}")
                                _state.value = NoteListFragmentState.NoteDeleted(result.data)
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

    fun logout() {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = logoutUseCase()) {
                is Resource.Success -> {
                    withContext(Dispatchers.Main) {
                        setLoading(false)
                        Log.d(TAG, "logout successfully: ${result.data}")
                        _state.value = NoteListFragmentState.Logout(result.data)
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

    fun getFirebaseCurrentUser(): FirebaseUser? {
        return getFirebaseCurrentUserUseCase()
    }
}

sealed class NoteListFragmentState {
    object Init : NoteListFragmentState()
    data class IsLoading(val isLoading: Boolean) : NoteListFragmentState()
    data class ShowToast(val message: String) : NoteListFragmentState()
    data class ShowError(val message: String) : NoteListFragmentState()
    data class Notes(val notes: List<Note>) : NoteListFragmentState()
    data class NoteDeleted(val message: String) : NoteListFragmentState()
    data class Logout(val message: String) : NoteListFragmentState()
}
