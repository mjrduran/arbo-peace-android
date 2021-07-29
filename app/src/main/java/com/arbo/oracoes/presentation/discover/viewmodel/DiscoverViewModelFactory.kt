package com.arbo.oracoes.presentation.discover.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arbo.oracoes.domain.usecase.AudioCategoryFindAllUseCase

class DiscoverViewModelFactory(private val audioCategoryFindAllUseCase: AudioCategoryFindAllUseCase) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiscoverViewModel::class.java)) {
            return DiscoverViewModel(audioCategoryFindAllUseCase) as T
        }
        return super.create(modelClass)
    }

}