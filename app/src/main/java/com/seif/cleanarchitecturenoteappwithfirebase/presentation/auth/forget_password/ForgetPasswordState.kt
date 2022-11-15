package com.seif.cleanarchitecturenoteappwithfirebase.presentation.auth.forget_password

sealed class ForgetPasswordState {
    object Init : ForgetPasswordState()
    data class IsLoading(val isLoading: Boolean) : ForgetPasswordState()
    data class ShowError(val message: String) : ForgetPasswordState()
    data class SendEmail(val message: String) : ForgetPasswordState()
}
