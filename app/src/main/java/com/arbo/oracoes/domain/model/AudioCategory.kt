package com.arbo.oracoes.domain.model

import java.io.Serializable

data class AudioCategory(val title: String,
                         val audios: List<String>): Serializable