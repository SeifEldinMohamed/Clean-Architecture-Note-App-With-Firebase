package com.seif.cleanarchitecturenoteappwithfirebase.domain.usecase

import com.google.firebase.auth.FirebaseUser
import com.seif.cleanarchitecturenoteappwithfirebase.domain.repository.AuthRepository
import javax.inject.Inject

class GetFirebaseCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): FirebaseUser? {
        return authRepository.getFirebaseCurrentUser()
    }
}