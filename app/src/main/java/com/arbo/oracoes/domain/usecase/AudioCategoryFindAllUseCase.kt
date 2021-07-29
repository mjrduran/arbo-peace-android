package com.arbo.oracoes.domain.usecase

import com.arbo.oracoes.domain.model.AudioCategory
import com.arbo.oracoes.domain.repository.AudioCategoryRepository
import com.arbo.oracoes.util.ioTask

class AudioCategoryFindAllUseCase(private val audioCategoryRepository: AudioCategoryRepository) {

    suspend fun execute(): List<AudioCategory> {
        return ioTask {
            audioCategoryRepository.findAll()
        }
    }

}