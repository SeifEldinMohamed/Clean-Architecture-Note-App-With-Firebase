package com.seif.cleanarchitecturenoteappwithfirebase.presentation.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seif.cleanarchitecturenoteappwithfirebase.domain.model.User
import com.seif.cleanarchitecturenoteappwithfirebase.domain.usecase.RegisterUseCase
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCAse: RegisterUseCase
) : ViewModel() {
    private var _registerState = MutableStateFlow<RegisterFragmentState>(RegisterFragmentState.Init)
    val registerState: StateFlow<RegisterFragmentState> = _registerState

    private fun showError(message: String) {
        _registerState.value = RegisterFragmentState.ShowError(message)
    }

    private fun setLoading(isLoading: Boolean) {
        when (isLoading) {
            true -> _registerState.value = RegisterFragmentState.IsLoading(true)
            false -> _registerState.value = RegisterFragmentState.IsLoading(false)
        }
    }

    fun registerUser(user: User) {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            registerUseCAse(user).collect {
                when (it) {
                    is Resource.Success -> {
                        withContext(Dispatchers.Main) {
                            setLoading(false)
                            _registerState.value = RegisterFragmentState.RegisteredSuccessfully(it.data)
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
}

// collect: Accepts the given collector and emits values into it.
// onEach: Returns a flow that invokes the given [action] **before** each value of the upstream flow is emitted downstream.