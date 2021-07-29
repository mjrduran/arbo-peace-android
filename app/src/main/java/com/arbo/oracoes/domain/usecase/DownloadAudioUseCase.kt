package com.arbo.oracoes.domain.usecase

import android.net.Uri
import com.arbo.oracoes.domain.model.Audio
import com.arbo.oracoes.domain.model.DownloadedAudio
import com.arbo.oracoes.domain.repository.StorageRepository
import com.arbo.oracoes.presentation.util.NetworkStateRepository
import com.arbo.oracoes.util.ioTask
import com.crashlytics.android.Crashlytics
import java.io.File

class DownloadAudioUseCase(
    private val storageRepository: StorageRepository,
    private val networkStateRepository: NetworkStateRepository
) {

    private var isConnectedToInternet: Boolean = true

    init {
        val disposable = networkStateRepository.connectionStatusEvent.subscribe({ isConnected ->
            isConnectedToInternet = isConnected
        }, {
            Crashlytics.logException(it)
        })
    }

    suspend fun downloadAudioFile(audio: Audio): DownloadedAudio {
        return ioTask {
            if (isConnectedToInternet){
                val uri = storageRepository.getDownloadUrl(audio.mediaUrl)
                val file = storageRepository.downloadAudioFile(audio.id, audio.mediaUrl)
                DownloadedAudio(audio, isDownloaded = true, file = file, uri = uri)
            } else {
                throw Exception("Not internet connection")
            }
        }
    }

    suspend fun getDownloadStatus(audio: Audio): DownloadedAudio {
        return ioTask {
            val file = storageRepository.getFile(audio.id)
            var uri: Uri? = null
            if (!file.exists()){
                uri = if (isConnectedToInternet) {
                    storageRepository.getDownloadUrl(audio.mediaUrl)
                } else {
                    null
                }
            }
            DownloadedAudio(audio, isDownloaded = file.exists(), file = file, uri = uri)
        }
    }

    suspend fun deleteFile(file: File): Boolean {
        return ioTask {
            try {
                if (file.exists()) {
                    return@ioTask file.delete()
                }
                return@ioTask false
            } catch (e: Exception) {
                Crashlytics.logException(e)
                return@ioTask false
            }
        }
    }
}