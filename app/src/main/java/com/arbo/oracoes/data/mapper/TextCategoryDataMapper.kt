package com.arbo.oracoes.data.mapper

import com.arbo.oracoes.data.model.TextCategoryData
import com.arbo.oracoes.domain.model.TextCategory

object TextCategoryDataMapper {
    fun map(textCategoryData: TextCategoryData?): TextCategory {
        return TextCategory(
            title = textCategoryData?.title ?: "",
            texts = textCategoryData?.texts ?: emptyList()
        )
    }
}