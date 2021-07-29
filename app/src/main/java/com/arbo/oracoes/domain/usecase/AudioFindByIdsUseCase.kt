package com.arbo.oracoes.domain.usecase

import com.arbo.oracoes.domain.model.DownloadedAudio
import com.arbo.oracoes.domain.repository.AudioRepository
import com.arbo.oracoes.domain.repository.StorageRepository
import com.arbo.oracoes.util.ioTask

class AudioFindByIdsUseCase(private val audioRepository: AudioRepository) {

    suspend fun execute(ids: List<String>): List<DownloadedAudio> = ioTask {
        audioRepository.findByIds(ids).map {
            DownloadedAudio(it, isDownloaded = false, file = null, uri = null)
        }
    }
}