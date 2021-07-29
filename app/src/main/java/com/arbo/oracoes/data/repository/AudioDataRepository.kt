package com.arbo.oracoes.data.repository

import com.arbo.oracoes.data.mapper.AudioDataMapper
import com.arbo.oracoes.data.model.AudioData
import com.arbo.oracoes.domain.model.Audio
import com.arbo.oracoes.domain.repository.AudioRepository
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AudioDataRepository(private val firestore: FirebaseFirestore) : AudioRepository {

    override suspend fun findByIds(ids: List<String>): List<Audio> {
        return suspendCoroutine { continuation ->
            firestore.collection(AUDIO_COLLECTION)
                .whereIn(FieldPath.documentId(), ids)
                .get().addOnSuccessListener { snapshot ->
                    try {
                        continuation.resume(snapshot.documents.map {
                            AudioDataMapper.map(it.id, it.toObject(AudioData::class.java))
                        })
                    } catch (ex: Exception) {
                        continuation.resumeWithException(ex)
                    }
                }.addOnFailureListener { ex ->
                    continuation.resumeWithException(ex)
                }
        }
    }

    override suspend fun getWeeklyAudio(): Audio {
        return suspendCoroutine { continuation ->
            firestore.collection(WEEKLY_AUDIO_COLLECTION).document(WEEKLY_AUDIO_DOC).get()
                .addOnSuccessListener { documentSnapshot ->
                    try {
                        val audioId: String = documentSnapshot.get("audio_id") as String
                        firestore.collection(AUDIO_COLLECTION).document(audioId).get()
                            .addOnSuccessListener {
                                continuation.resume(
                                    AudioDataMapper.map(
                                        it.id,
                                        it.toObject(AudioData::class.java)
                                    )
                                )
                            }.addOnFailureListener { ex ->
                                continuation.resumeWithException(ex)
                            }
                    } catch (ex: Exception) {
                        continuation.resumeWithException(ex)
                    }
                }.addOnFailureListener { ex ->
                    continuation.resumeWithException(ex)
                }

        }
    }

    companion object {
        const val AUDIO_COLLECTION = "audios"
        const val WEEKLY_AUDIO_COLLECTION = "weekly_audio"
        const val WEEKLY_AUDIO_DOC = "weekly_audio_index"
    }
}