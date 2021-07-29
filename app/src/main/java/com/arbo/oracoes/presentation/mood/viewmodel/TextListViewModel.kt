package com.arbo.oracoes.presentation.mood.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arbo.barbeiro.presentation.base.BaseViewModel
import com.arbo.oracoes.domain.model.Text
import com.arbo.oracoes.domain.usecase.TextFindByIdsUseCase

class TextListViewModel(
    private val textFindByIdsUseCase: TextFindByIdsUseCase

) :
    BaseViewModel() {

    private val _texts = MutableLiveData<List<Text>>()

    val texts: LiveData<List<Text>> = _texts

    fun loadAudios(ids: List<String>) {
        executeTaskWithLoading {
            _texts.value = textFindByIdsUseCase.execute(ids)
        }
    }
}