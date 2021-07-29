package com.arbo.oracoes.presentation.more.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arbo.barbeiro.presentation.base.BaseViewModel
import com.arbo.oracoes.domain.model.analytics.DisableDarkTheme
import com.arbo.oracoes.domain.model.analytics.EnableDarkTheme
import com.arbo.oracoes.domain.repository.AnalyticsRepository
import com.arbo.oracoes.domain.repository.PreferencesRepository
import com.arbo.oracoes.util.content.UploadContent

class MoreViewModel(
    private val uploadContent: UploadContent,
    private val preferencesRepository: PreferencesRepository,
    private val analyticsRepository: AnalyticsRepository
) : BaseViewModel() {

    private val _isDarkThemeEnabled = MutableLiveData<Boolean>()

    val isDarkThemeEnable: LiveData<Boolean> = _isDarkThemeEnabled

    fun enableOrDisableDarkTheme(enable: Boolean) {
        executeTask {
            if (enable) {
                preferencesRepository.enabledDarkTheme()
                _isDarkThemeEnabled.value = true
                trackDarkTheme(enable)
            } else {
                preferencesRepository.disableDarkTheme()
                _isDarkThemeEnabled.value = false
                trackDarkTheme(false)
            }
        }
    }

    fun loadDarkModeState() {
        executeTask {
            _isDarkThemeEnabled.value = preferencesRepository.isDarkThemeEnabled()
        }
    }

    fun uploadAudios() {
        executeTaskWithLoading {
            uploadContent.saveTexts()
        }
    }

    fun uploadAudioCategories() {
        executeTaskWithLoading {
            uploadContent.saveAudioCategories()
        }
    }

    private fun trackDarkTheme(enabled: Boolean) {
        if (enabled) {
            analyticsRepository.trackEvent(EnableDarkTheme())
        } else {
            analyticsRepository.trackEvent(DisableDarkTheme())
        }
    }

}