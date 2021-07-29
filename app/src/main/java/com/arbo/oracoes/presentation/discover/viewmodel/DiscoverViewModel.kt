package com.arbo.oracoes.presentation.discover.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arbo.barbeiro.presentation.base.BaseViewModel
import com.arbo.oracoes.domain.model.AudioCategory
import com.arbo.oracoes.domain.usecase.AudioCategoryFindAllUseCase

class DiscoverViewModel(private val audioCategoryFindAllUseCase: AudioCategoryFindAllUseCase) :
    BaseViewModel() {

    private val _audioCategories = MutableLiveData<List<AudioCategory>>()

    val audioCategories: LiveData<List<AudioCategory>> = _audioCategories

    fun loadCategories() {
        executeTaskWithLoading {
            _audioCategories.value = audioCategoryFindAllUseCase.execute()
        }
    }

}