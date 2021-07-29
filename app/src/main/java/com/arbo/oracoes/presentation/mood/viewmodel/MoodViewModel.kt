package com.arbo.oracoes.presentation.mood.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arbo.barbeiro.presentation.base.BaseViewModel
import com.arbo.oracoes.domain.model.TextCategory
import com.arbo.oracoes.domain.usecase.TextCategoryFindAllUseCase

class MoodViewModel(private val categoryFindAllUseCase: TextCategoryFindAllUseCase) :
    BaseViewModel() {

    private val _categories = MutableLiveData<List<TextCategory>>()

    val categories: LiveData<List<TextCategory>> = _categories

    fun loadCategories() {
        executeTaskWithLoading {
            _categories.value = categoryFindAllUseCase.execute()
        }
    }
}