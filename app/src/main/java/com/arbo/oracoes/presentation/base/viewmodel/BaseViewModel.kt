package com.arbo.barbeiro.presentation.base

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope


import com.arbo.oracoes.R
import com.arbo.oracoes.presentation.base.model.ValidationMessage
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class BaseViewModel : ViewModel() {

    protected val _validationMessage = MutableLiveData<ValidationMessage>()
    val validationMessage: LiveData<ValidationMessage> = _validationMessage

    protected val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun executeTask(codeBlock: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.Main) {
                    codeBlock()
                }
            } catch (e: Exception) {
                _validationMessage.value = ValidationMessage(
                    titleRes = R.string.generic_error_title,
                    messageRes = R.string.generic_error_message,
                    endFlow = false
                )
                Log.e(TAG, "Error loading entity", e)
            }
        }
    }

    fun executeTaskWithLoading(codeBlock: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                _loading.value = true
                withContext(Dispatchers.Main) {
                    codeBlock()
                }
                _loading.value = false
            } catch (e: Exception) {
                _loading.value = false
                _validationMessage.value = ValidationMessage(
                    titleRes = R.string.generic_error_title,
                    messageRes = R.string.generic_error_message,
                    endFlow = false
                )
                Log.e(TAG, "Error loading entity", e)
            }
        }
    }

}