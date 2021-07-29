package com.arbo.oracoes.data.mapper

import com.arbo.oracoes.data.model.TextData
import com.arbo.oracoes.domain.model.Text

object TextDataMapper {
    fun map(id: String, data: TextData?) = Text(
        id = id,
        title = data?.title ?: "",
        image = data?.image ?: "",
        body = data?.body ?: ""
    )
}