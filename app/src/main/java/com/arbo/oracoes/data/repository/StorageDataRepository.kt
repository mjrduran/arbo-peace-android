package com.arbo.oracoes.data.repository

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.arbo.oracoes.domain.repository.StorageRepository
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class StorageDataRepository(
    private val firebaseStorage: FirebaseStorage,
    private val context: Context
) : StorageRepository {

    override suspend fun downloadAudioFile(fileName: String, storageUrl: String): File {
        return suspendCoroutine { continuation ->
            try {
                val parentDir = File(
                    context.getExternalFilesDir(Environment.DIRECTORY_MUSIC),
                    "$AUDIO_FOLDER/"
                )
                if (!parentDir.exists()) {
                    parentDir.mkdirs()
                }
                val file = File(
                    context.getExternalFilesDir(Environment.DIRECTORY_MUSIC),
                    "$AUDIO_FOLDER/$fileName"
                )
                if (file.exists()) {
                    continuation.resume(file)
                } else {
                    val gsReference = firebaseStorage.getReferenceFromUrl(storageUrl)
                    gsReference.getFile(file).addOnSuccessListener {
                        continuation.resume(file)
                    }.addOnFailureListener { ex ->
                        continuation.resumeWithException(ex)
                    }
                }
            } catch (e: Exception) {
                continuation.resumeWithException(e)
            }
        }
    }

    override suspend fun getDownloadUrl(storageUrl: String): Uri {
        return suspendCoroutine { continuation ->
            try {
                val gsReference = firebaseStorage.getReferenceFromUrl(storageUrl)
                gsReference.downloadUrl.addOnSuccessListener {
                    continuation.resume(it)
                }.addOnFailureListener {ex ->
                    continuation.resumeWithException(ex)
                }
            } catch (e: Exception) {
                continuation.resumeWithException(e)
            }
        }
    }

    override suspend fun getFile(fileName: String): File {
        return File(
            context.getExternalFilesDir(Environment.DIRECTORY_MUSIC),
            "$AUDIO_FOLDER/$fileName"
        )
    }

    companion object {
        private const val AUDIO_FOLDER = "audio_messages"
    }
}