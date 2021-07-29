package com.arbo.oracoes.data.repository

import com.arbo.oracoes.data.mapper.TextDataMapper
import com.arbo.oracoes.data.model.TextData
import com.arbo.oracoes.domain.model.Text
import com.arbo.oracoes.domain.repository.TextRepository
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class TextDataRepository(private val firestore: FirebaseFirestore) : TextRepository {

    override suspend fun findByIds(ids: List<String>): List<Text> {
        return suspendCoroutine { continuation ->
            firestore.collection(TEXTS_COLLECTION)
                .whereIn(FieldPath.documentId(), ids)
                .get().addOnSuccessListener { snapshot ->
                    try {
                        val result = snapshot.documents.map {
                            TextDataMapper.map(it.id, it.toObject(TextData::class.java))
                        }
                        continuation.resume(result)
                    } catch (ex: Exception) {
                        continuation.resumeWithException(ex)
                    }
                }.addOnFailureListener { ex ->
                    continuation.resumeWithException(ex)
                }
        }
    }

    override suspend fun getDailyText(): Text {
        return suspendCoroutine { continuation ->
            firestore.collection(DAILY_TEXT_COLLECTION).document(DAILY_TEXT_DOC).get()
                .addOnSuccessListener { documentSnapshot ->
                    try {
                        val textId: String = documentSnapshot.get("text_id") as String
                        firestore.collection(TEXTS_COLLECTION).document(textId).get()
                            .addOnSuccessListener {
                                continuation.resume(
                                    TextDataMapper.map(
                                        it.id,
                                        it.toObject(TextData::class.java)
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
        const val TEXTS_COLLECTION = "texts"
        const val DAILY_TEXT_COLLECTION = "daily_text"
        const val DAILY_TEXT_DOC = "daily_text_index"
    }
}