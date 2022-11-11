package com.seif.cleanarchitecturenoteappwithfirebase.domain.usecase

import com.seif.cleanarchitecturenoteappwithfirebase.domain.model.User
import com.seif.cleanarchitecturenoteappwithfirebase.domain.repository.AuthRepository
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Resource
import com.seif.cleanarchitecturenoteappwithfirebase.utils.isValidUserInput
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(user: User) = flow {
        when (val result = user.isValidUserInput()) {
            is Resource.Success -> {
                authRepository.registerUser(user).collect {
                    emit(it)
                }
            }
            is Resource.Error -> {
                emit(Resource.Error(result.message))
            }
        }
    }
}