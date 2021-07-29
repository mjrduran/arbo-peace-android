package com.arbo.oracoes.presentation.more.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arbo.barbeiro.presentation.base.BaseViewModel
import com.arbo.oracoes.domain.model.ReminderTime
import com.arbo.oracoes.domain.repository.PreferencesRepository
import com.arbo.oracoes.domain.repository.ReminderSchedulerRepository

class ReminderViewModel(
    private val preferencesRepository: PreferencesRepository,
    private val schedulerRepository: ReminderSchedulerRepository
) : BaseViewModel() {

    private val _reminderTime = MutableLiveData<ReminderTime>()

    val reminderTime: LiveData<ReminderTime> = _reminderTime

    private val _reminderEnabled = MutableLiveData<Boolean>()

    val reminderEnabled: LiveData<Boolean> = _reminderEnabled

    fun loadReminderTime() {
        executeTask {
            _reminderTime.value = preferencesRepository.getDailyReminderTime()
            _reminderEnabled.value = preferencesRepository.isReminderEnabled()
        }
    }

    fun setReminderTime(hour: Int, minute: Int) {
        executeTask {
            val newTime = ReminderTime(hour, minute)
            _reminderTime.value = newTime
            preferencesRepository.setDailyReminderTime(newTime)
            reScheduleReminder()
        }
    }

    fun cancelReminder() {
        executeTask {
            preferencesRepository.disableReminder()
            schedulerRepository.cancelDailyReminder()
        }
    }

    fun reScheduleReminder() {
        executeTask {
            preferencesRepository.enabledReminder()
            reminderTime.value?.let {
                schedulerRepository.reScheduleDailyReminder(
                    it.hour,
                    it.minute
                )
            }
        }
    }
}