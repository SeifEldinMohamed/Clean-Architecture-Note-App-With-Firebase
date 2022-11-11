package com.seif.cleanarchitecturenoteappwithfirebase.domain.usecase

import com.seif.cleanarchitecturenoteappwithfirebase.domain.repository.AuthRepository
import com.seif.cleanarchitecturenoteappwithfirebase.utils.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String, password: String) = flow {
        when (val result = isValidEmailAndPassword(email, password)) {
            is Resource.Success -> {
                authRepository.loginUser(email, password).collect {
                    emit(it)
                }
            }
            is Resource.Error -> emit(result)
        }
    }
}