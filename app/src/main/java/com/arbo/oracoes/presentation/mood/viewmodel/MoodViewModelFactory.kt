package com.arbo.oracoes.presentation.mood.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arbo.oracoes.domain.usecase.TextCategoryFindAllUseCase

class MoodViewModelFactory(private val categoryFindAllUseCase: TextCategoryFindAllUseCase) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoodViewModel::class.java)) {
            return MoodViewModel(categoryFindAllUseCase) as T
        }
        return super.create(modelClass)
    }

}