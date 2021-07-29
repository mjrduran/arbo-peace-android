package com.arbo.oracoes.data.repository

import com.arbo.oracoes.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthDataRepository(val firebaseAuth: FirebaseAuth) : AuthRepository {

    override suspend fun signInAnonymously(): Boolean {
        return suspendCoroutine { continuation ->
            firebaseAuth.signInAnonymously().addOnSuccessListener {
                continuation.resume(true)
            }.addOnFailureListener { ex ->
                continuation.resumeWithException(ex)
            }
        }
    }

}