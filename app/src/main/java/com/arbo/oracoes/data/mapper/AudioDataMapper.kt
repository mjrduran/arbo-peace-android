package com.arbo.oracoes.data.mapper

import com.arbo.oracoes.data.model.AudioData
import com.arbo.oracoes.domain.model.Audio

object AudioDataMapper {
    fun map(id: String, audioData: AudioData?) = Audio(
        id = id,
        mediaUrl = audioData?.media_url ?: "",
        title = audioData?.title ?: "",
        image = audioData?.image ?: ""
    )
}