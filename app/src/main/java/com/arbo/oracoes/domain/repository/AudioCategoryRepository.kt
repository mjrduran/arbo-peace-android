package com.arbo.oracoes.domain.repository

import com.arbo.oracoes.domain.model.AudioCategory

interface AudioCategoryRepository {

    suspend fun findAll(): List<AudioCategory>

}