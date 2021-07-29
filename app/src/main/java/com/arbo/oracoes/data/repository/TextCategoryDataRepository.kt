package com.arbo.oracoes.data.repository

import com.arbo.oracoes.data.mapper.TextCategoryDataMapper
import com.arbo.oracoes.data.model.TextCategoryData
import com.arbo.oracoes.domain.model.TextCategory
import com.arbo.oracoes.domain.repository.TextCategoryRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class TextCategoryDataRepository(private val firestore: FirebaseFirestore) :
    TextCategoryRepository {

    companion object {
        private const val CATEGORY_COLLECTION = "text_categories"
    }

    override suspend fun findAll(): List<TextCategory> {
        return suspendCoroutine { continuation ->
            firestore.collection(CATEGORY_COLLECTION).get()
                .addOnSuccessListener { querySnapshot ->
                    try {
                        val categoryList = querySnapshot.documents.map { snapshot ->
                            TextCategoryDataMapper.map(snapshot.toObject(TextCategoryData::class.java))
                        }
                        continuation.resume(categoryList)
                    } catch (ex: Exception) {
                        continuation.resumeWithException(ex)
                    }
                }.addOnFailureListener { ex ->
                    continuation.resumeWithException(ex)
                }
        }
    }
}