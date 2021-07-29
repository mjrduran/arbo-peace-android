package com.arbo.oracoes.presentation.more.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arbo.oracoes.domain.repository.PreferencesRepository
import com.arbo.oracoes.domain.repository.ReminderSchedulerRepository

class ReminderViewModelFactory(
    private val preferencesRepository: PreferencesRepository,
    private val schedulerRepository: ReminderSchedulerRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReminderViewModel::class.java)) {
            return ReminderViewModel(preferencesRepository, schedulerRepository) as T
        }
        return super.create(modelClass)
    }

}