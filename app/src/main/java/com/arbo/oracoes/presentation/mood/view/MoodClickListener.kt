package com.arbo.oracoes.presentation.mood.view

import com.arbo.oracoes.domain.model.TextCategory

interface MoodClickListener {

    fun onClick(textCategory: TextCategory)

}