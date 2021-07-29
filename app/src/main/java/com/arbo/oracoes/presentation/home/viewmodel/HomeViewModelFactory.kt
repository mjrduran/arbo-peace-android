package com.arbo.oracoes.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arbo.oracoes.domain.usecase.*

class HomeViewModelFactory(private val dailyTextUseCase: DailyTextUseCase,
                           private val weeklyAudioUseCase: WeeklyAudioUseCase,
                           private val downloadAudioUseCase: DownloadAudioUseCase,
                           private val signInAnonymouslyUseCase: SignInAnonymouslyUseCase
) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(dailyTextUseCase,
                weeklyAudioUseCase,
                downloadAudioUseCase,
                signInAnonymouslyUseCase) as T
        }
        return super.create(modelClass)
    }

}