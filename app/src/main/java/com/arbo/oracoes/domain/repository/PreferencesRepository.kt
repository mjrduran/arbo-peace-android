package com.arbo.oracoes.domain.repository

import com.arbo.oracoes.domain.model.ReminderTime

interface PreferencesRepository {

    fun setDailyReminderTime(time: ReminderTime)

    fun getDailyReminderTime(): ReminderTime

    fun isReminderEnabled(): Boolean

    fun enabledReminder()

    fun disableReminder()

    fun isDarkThemeEnabled(): Boolean

    fun enabledDarkTheme()

    fun disableDarkTheme()

}