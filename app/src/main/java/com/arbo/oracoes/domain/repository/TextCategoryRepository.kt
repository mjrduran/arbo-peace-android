package com.arbo.oracoes.domain.repository

import com.arbo.oracoes.domain.model.TextCategory

interface TextCategoryRepository {

    suspend fun findAll(): List<TextCategory>

}