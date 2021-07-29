package com.arbo.oracoes.presentation.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arbo.barbeiro.presentation.base.BaseViewModel
import com.arbo.oracoes.domain.model.Audio
import com.arbo.oracoes.domain.model.DownloadedAudio
import com.arbo.oracoes.domain.model.Text
import com.arbo.oracoes.domain.usecase.DailyTextUseCase
import com.arbo.oracoes.domain.usecase.DownloadAudioUseCase
import com.arbo.oracoes.domain.usecase.SignInAnonymouslyUseCase
import com.arbo.oracoes.domain.usecase.WeeklyAudioUseCase

class HomeViewModel(
    private val dailyTextUseCase: DailyTextUseCase,
    private val weeklyAudioUseCase: WeeklyAudioUseCase,
    private val downloadAudioUseCase: DownloadAudioUseCase,
    private val signInAnonymouslyUseCase: SignInAnonymouslyUseCase
) :
    BaseViewModel() {

    private val _dailyText = MutableLiveData<Text>()

    val dailyText: LiveData<Text> = _dailyText

    private val _dailyTextLoading = MutableLiveData<Boolean>(true)

    val dailyTextLoading: LiveData<Boolean> = _dailyTextLoading


    private val _weeklyAudio = MutableLiveData<DownloadedAudio>()

    val weeklyAudio: LiveData<DownloadedAudio> = _weeklyAudio

    private val _weeklyAudioLoading = MutableLiveData<Boolean>(true)

    val weeklyAudioLoading: LiveData<Boolean> = _weeklyAudioLoading

    private val _audioDownload = MutableLiveData<DownloadedAudio>()

    val audioDownload: LiveData<DownloadedAudio> = _audioDownload

    private val _isSignedIn = MutableLiveData<Boolean>()

    val isSignedIn: LiveData<Boolean> = _isSignedIn

    fun signInAnonymously (){
        executeTask {
            _isSignedIn.value = signInAnonymouslyUseCase.execute()
        }
    }

    fun loadDailyText() {
        executeTask {
            try {
                _dailyText.value = dailyTextUseCase.execute()
            } finally {
                _dailyTextLoading.value = false
            }
        }
    }

    fun loadWeeklyAudio() {
        executeTask {
            try {
                _weeklyAudio.value = weeklyAudioUseCase.execute()
            } finally {
                _weeklyAudioLoading.value = false
            }
        }
    }

    fun downloadAudio(audio: Audio) {
        executeTask {
            try {
                _weeklyAudioLoading.value = true
                _audioDownload.value = downloadAudioUseCase.downloadAudioFile(audio)
            } finally {
                _weeklyAudioLoading.value = false
            }
        }
    }

}