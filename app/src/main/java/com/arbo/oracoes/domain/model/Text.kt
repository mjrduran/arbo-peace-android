package com.arbo.oracoes.domain.model

import java.io.Serializable

data class Text(
    val id: String,
    val title: String,
    val image: String,
    val body: String
) : Serializable