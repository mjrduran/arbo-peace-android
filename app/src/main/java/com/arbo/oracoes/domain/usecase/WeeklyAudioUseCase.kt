package com.arbo.oracoes.domain.usecase

import com.arbo.oracoes.domain.model.DownloadedAudio
import com.arbo.oracoes.domain.repository.AudioRepository
import com.arbo.oracoes.domain.repository.StorageRepository
import com.arbo.oracoes.util.ioTask

class WeeklyAudioUseCase(private val audioRepository: AudioRepository,
                         private val storageRepository: StorageRepository
) {

    suspend fun execute(): DownloadedAudio = ioTask {
        val weeklyAudio = audioRepository.getWeeklyAudio()
        val file = storageRepository.getFile(weeklyAudio.id)
        DownloadedAudio(weeklyAudio, isDownloaded = file.exists(), file = file)
    }
}