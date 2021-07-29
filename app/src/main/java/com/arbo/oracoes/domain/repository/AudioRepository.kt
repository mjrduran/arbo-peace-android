package com.arbo.oracoes.domain.repository

import com.arbo.oracoes.domain.model.Audio

interface AudioRepository {

    suspend fun findByIds(ids: List<String>): List<Audio>

    suspend fun getWeeklyAudio(): Audio
}