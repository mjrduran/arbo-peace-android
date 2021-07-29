package com.arbo.oracoes.data.repository

import com.arbo.oracoes.data.mapper.AudioCategoryDataMapper
import com.arbo.oracoes.data.model.AudioCategoryData
import com.arbo.oracoes.domain.model.AudioCategory
import com.arbo.oracoes.domain.repository.AudioCategoryRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AudioCategoryDataRepository(private val firestore: FirebaseFirestore) :
    AudioCategoryRepository {

    companion object {
        private const val AUDIO_CATEGORY_COLLECTION = "audio_categories"
    }

    override suspend fun findAll(): List<AudioCategory> {
        return suspendCoroutine { continuation ->
            firestore.collection(AUDIO_CATEGORY_COLLECTION).orderBy("title").get()
                .addOnSuccessListener { querySnapshot ->
                    try {
                        val audioCategoryList = querySnapshot.documents.map { snapshot ->
                            AudioCategoryDataMapper.map(snapshot.toObject(AudioCategoryData::class.java))
                        }
                        continuation.resume(audioCategoryList)
                    } catch (ex: Exception) {
                        continuation.resumeWithException(ex)
                    }
                }.addOnFailureListener { ex ->
                    continuation.resumeWithException(ex)
                }
        }
    }
}