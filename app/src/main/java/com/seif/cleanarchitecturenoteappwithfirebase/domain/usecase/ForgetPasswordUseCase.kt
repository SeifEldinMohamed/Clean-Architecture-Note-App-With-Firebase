package com.seif.cleanarchitecturenoteappwithfirebase.domain.usecase

import com.seif.cleanarchitecturenoteappwithfirebase.domain.repository.AuthRepository
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Resource
import com.seif.cleanarchitecturenoteappwithfirebase.utils.isValidEmailInput
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ForgetPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String) = flow {
        when (val result = isValidEmailInput(email)) {
            is Resource.Success -> authRepository.forgetPassword(email).collect {
                emit(it)
            }
            is Resource.Error -> emit(result)
        }
    }
}