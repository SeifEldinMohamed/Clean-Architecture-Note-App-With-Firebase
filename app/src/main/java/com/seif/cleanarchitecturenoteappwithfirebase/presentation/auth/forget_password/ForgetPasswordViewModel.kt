package com.seif.cleanarchitecturenoteappwithfirebase.presentation.auth.forget_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seif.cleanarchitecturenoteappwithfirebase.domain.usecase.ForgetPasswordUseCase
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ForgetPasswordViewModel @Inject constructor(
    private val forgetPasswordUseCase: ForgetPasswordUseCase
) : ViewModel() {
    private var _forgetPasswordState = MutableStateFlow<ForgetPasswordState>(ForgetPasswordState.Init)
    val forgetPasswordState: StateFlow<ForgetPasswordState> = _forgetPasswordState

    private fun showError(message: String) {
        _forgetPasswordState.value = ForgetPasswordState.ShowError(message)
    }

    private fun setLoading(isLoading: Boolean) {
        when (isLoading) {
            true -> _forgetPasswordState.value = ForgetPasswordState.IsLoading(true)
            false -> _forgetPasswordState.value = ForgetPasswordState.IsLoading(false)
        }
    }

    fun forgetPassword(email: String) {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            forgetPasswordUseCase(email).collect {
                when (it) {
                    is Resource.Success -> {
                        withContext(Dispatchers.Main) {
                            setLoading(false)
                            _forgetPasswordState.value = ForgetPasswordState.SendEmail(it.data)
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