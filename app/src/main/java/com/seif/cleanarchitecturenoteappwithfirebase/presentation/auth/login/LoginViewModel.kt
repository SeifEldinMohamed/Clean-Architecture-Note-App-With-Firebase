package com.seif.cleanarchitecturenoteappwithfirebase.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seif.cleanarchitecturenoteappwithfirebase.domain.usecase.LoginUseCase
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    private var _loginState = MutableStateFlow<LoginFragmentState>(LoginFragmentState.Init)
    val loginState: StateFlow<LoginFragmentState> = _loginState

    private fun showError(message: String) {
        _loginState.value = LoginFragmentState.ShowError(message)
    }

    private fun setLoading(isLoading: Boolean) {
        when (isLoading) {
            true -> _loginState.value = LoginFragmentState.IsLoading(true)
            false -> _loginState.value = LoginFragmentState.IsLoading(false)
        }
    }

    fun loginUser(email: String, password: String) {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            loginUseCase(email, password).collect {
                when (it) {
                    is Resource.Success -> {
                        withContext(Dispatchers.Main) {
                            setLoading(false)
                            _loginState.value = LoginFragmentState.LoggedInSuccessfully(it.data)
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