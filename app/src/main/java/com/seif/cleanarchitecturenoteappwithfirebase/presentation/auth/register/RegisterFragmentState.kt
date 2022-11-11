package com.seif.cleanarchitecturenoteappwithfirebase.presentation.auth.register

import com.seif.cleanarchitecturenoteappwithfirebase.domain.model.User

sealed class RegisterFragmentState {
    object Init : RegisterFragmentState()
    data class IsLoading(val isLoading: Boolean) : RegisterFragmentState()
    data class ShowError(val message: String) : RegisterFragmentState()
    data class RegisteredSuccessfully(val registeredUser: User) : RegisterFragmentState()
}
