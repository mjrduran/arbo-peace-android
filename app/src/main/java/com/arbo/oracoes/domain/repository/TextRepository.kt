package com.arbo.oracoes.domain.repository

import com.arbo.oracoes.domain.model.Text

interface TextRepository {

    suspend fun findByIds(ids: List<String>): List<Text>

    suspend fun getDailyText(): Text
}