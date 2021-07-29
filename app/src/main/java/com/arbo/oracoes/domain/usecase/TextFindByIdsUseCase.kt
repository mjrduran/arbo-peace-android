package com.arbo.oracoes.domain.usecase

import com.arbo.oracoes.domain.model.Text
import com.arbo.oracoes.domain.repository.TextRepository
import com.arbo.oracoes.util.ioTask

class TextFindByIdsUseCase(private val textRepository: TextRepository) {

    suspend fun execute(ids: List<String>): List<Text> = ioTask {
        textRepository.findByIds(ids)
    }
}