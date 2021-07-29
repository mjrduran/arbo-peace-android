package com.arbo.oracoes.presentation.discover.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arbo.oracoes.domain.usecase.AudioFindByIdsUseCase
import com.arbo.oracoes.domain.usecase.DownloadAudioUseCase

class AudioListViewModelFactory(private val audioFindByIdsUseCase: AudioFindByIdsUseCase,
                                private val downloadAudioUseCase: DownloadAudioUseCase) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AudioListViewModel::class.java)) {
            return AudioListViewModel(audioFindByIdsUseCase, downloadAudioUseCase) as T
        }
        return super.create(modelClass)
    }

}