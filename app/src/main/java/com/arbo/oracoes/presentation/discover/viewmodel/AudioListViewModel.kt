package com.arbo.oracoes.presentation.discover.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arbo.barbeiro.presentation.base.BaseViewModel
import com.arbo.oracoes.R
import com.arbo.oracoes.domain.model.Audio
import com.arbo.oracoes.domain.model.DownloadedAudio
import com.arbo.oracoes.domain.usecase.AudioFindByIdsUseCase
import com.arbo.oracoes.domain.usecase.DownloadAudioUseCase
import com.arbo.oracoes.presentation.base.model.ValidationMessage

class AudioListViewModel(
    private val audioFindByIdsUseCase: AudioFindByIdsUseCase,
    private val downloadAudioUseCase: DownloadAudioUseCase
) :
    BaseViewModel() {

    private val _audios = MutableLiveData<List<DownloadedAudio>>()

    val audios: LiveData<List<DownloadedAudio>> = _audios

    private val _audioDownload = MutableLiveData<DownloadedAudio>()

    val audioDownload: LiveData<DownloadedAudio> = _audioDownload

    private val _audioDelete = MutableLiveData<DownloadedAudio>()

    val audioDelete: LiveData<DownloadedAudio> = _audioDelete

    private val _audioUpdate = MutableLiveData<DownloadedAudio>()

    val audioWithDownloadStatus: LiveData<DownloadedAudio> = _audioUpdate

    fun loadAudios(ids: List<String>) {
        executeTaskWithLoading {
            _audios.value = audioFindByIdsUseCase.execute(ids)
        }
    }

    fun updateAudiosDownloadStatus() {
        executeTask {
            _audios.value?.forEach {
                _audioUpdate.value = downloadAudioUseCase.getDownloadStatus(it.audio)
            }
        }
    }

    fun downloadAudio(audio: Audio) {
        executeTask {
            try {
                _audioDownload.value = downloadAudioUseCase.downloadAudioFile(audio)

            } catch (e: Exception) {
                _audioDownload.value =
                    DownloadedAudio(audio, isDownloaded = false, file = null, uri = null)
                throw e
            }
        }
    }

    fun deleteAudio(downloadedAudio: DownloadedAudio) {
        executeTask {
            downloadedAudio.file?.let {
                if (downloadAudioUseCase.deleteFile(it)) {
                    _audioDelete.value = downloadAudioUseCase.getDownloadStatus(downloadedAudio.audio)
                }
                _validationMessage.value = ValidationMessage(
                    R.string.audio_delete_success_title,
                    R.string.audio_delete_success_message, endFlow = false, isSuccess = true
                )
            }
        }
    }
}