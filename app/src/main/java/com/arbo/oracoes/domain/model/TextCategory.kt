package com.arbo.oracoes.domain.model

import java.io.Serializable

data class TextCategory(val title: String,
                        val texts: List<String>): Serializable