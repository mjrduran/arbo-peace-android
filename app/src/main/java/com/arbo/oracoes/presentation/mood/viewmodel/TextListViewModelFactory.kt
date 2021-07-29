package com.arbo.oracoes.presentation.mood.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arbo.oracoes.domain.usecase.AudioFindByIdsUseCase
import com.arbo.oracoes.domain.usecase.DownloadAudioUseCase
import com.arbo.oracoes.domain.usecase.TextFindByIdsUseCase

class TextListViewModelFactory(private val textFindByIdsUseCase: TextFindByIdsUseCase) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TextListViewModel::class.java)) {
            return TextListViewModel(textFindByIdsUseCase) as T
        }
        return super.create(modelClass)
    }

}