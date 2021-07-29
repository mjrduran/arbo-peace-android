package com.arbo.oracoes.domain.usecase

import com.arbo.oracoes.domain.model.TextCategory
import com.arbo.oracoes.domain.repository.TextCategoryRepository
import com.arbo.oracoes.util.ioTask

class TextCategoryFindAllUseCase(private val categoryRepository: TextCategoryRepository) {

    suspend fun execute(): List<TextCategory> {
        return ioTask {
            categoryRepository.findAll()
        }
    }

}