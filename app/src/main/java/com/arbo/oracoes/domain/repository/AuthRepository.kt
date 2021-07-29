package com.arbo.oracoes.domain.repository

interface AuthRepository {

    suspend fun signInAnonymously(): Boolean

}