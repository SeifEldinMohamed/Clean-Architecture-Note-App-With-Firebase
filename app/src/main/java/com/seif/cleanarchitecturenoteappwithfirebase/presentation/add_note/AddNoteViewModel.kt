package com.seif.cleanarchitecturenoteappwithfirebase.presentation.add_note

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.seif.cleanarchitecturenoteappwithfirebase.domain.model.Note
import com.seif.cleanarchitecturenoteappwithfirebase.domain.usecase.AddNoteUseCase
import com.seif.cleanarchitecturenoteappwithfirebase.domain.usecase.GetFirebaseCurrentUserUseCase
import com.seif.cleanarchitecturenoteappwithfirebase.domain.usecase.UploadSingleImageUseCase
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val addNoteUseCase: AddNoteUseCase,
    private val getFirebaseCurrentUserUseCase: GetFirebaseCurrentUserUseCase,
    private val uploadSingleImageUseCase: UploadSingleImageUseCase
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

    fun getFirebaseCurrentUser(): FirebaseUser? {
        return getFirebaseCurrentUserUseCase()
    }

    fun uploadSingleImage(fileUri: Uri) {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            uploadSingleImageUseCase(fileUri).collect {
                when (it) {
                    is Resource.Success -> {
                        withContext(Dispatchers.Main) {
                            setLoading(false)
                            _state.value = AddNoteFragmentState.ImageUploaded(it.data)
                        }
                    }
                    is Resource.Error -> {
                        withContext(Dispatchers.Main) {
                            setLoading(false)
                            showError(it.message)
                        }
                    }
                }
            }
        }
    }

//    fun onUploadMultipleFile(fileUris: List<Uri>, onResult: (UiState<List<Uri>>) -> Unit){
//        onResult.invoke(UiState.Loading)
//        viewModelScope.launch {
//            repository.uploadMultipleFile(fileUris,onResult)
//        }
//    }
}
