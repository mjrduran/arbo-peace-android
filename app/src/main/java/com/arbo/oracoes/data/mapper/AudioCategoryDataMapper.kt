package com.arbo.oracoes.data.mapper

import com.arbo.oracoes.data.model.AudioCategoryData
import com.arbo.oracoes.domain.model.AudioCategory

object AudioCategoryDataMapper {
    fun map(audioCategoryData: AudioCategoryData?): AudioCategory {
        return AudioCategory(
            title = audioCategoryData?.title ?: "",
            audios = audioCategoryData?.audios ?: emptyList()
        )
    }
}