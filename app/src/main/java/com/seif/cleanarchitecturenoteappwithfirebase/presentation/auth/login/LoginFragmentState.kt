package com.seif.cleanarchitecturenoteappwithfirebase.presentation.auth.login

sealed class LoginFragmentState {
    object Init : LoginFragmentState()
    data class IsLoading(val isLoading: Boolean) : LoginFragmentState()
    data class ShowError(val message: String) : LoginFragmentState()
    data class LoggedInSuccessfully(val username: String) : LoginFragmentState()
}
