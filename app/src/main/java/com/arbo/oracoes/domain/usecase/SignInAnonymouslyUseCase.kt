package com.arbo.oracoes.domain.usecase

import com.arbo.oracoes.domain.repository.AuthRepository
import com.arbo.oracoes.util.ioTask

class SignInAnonymouslyUseCase(private val authRepository: AuthRepository) {
    suspend fun execute(): Boolean {
        return ioTask {
            authRepository.signInAnonymously()
        }
    }
}