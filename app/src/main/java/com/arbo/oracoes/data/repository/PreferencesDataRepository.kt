package com.arbo.oracoes.data.repository

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.arbo.oracoes.domain.model.ReminderTime
import com.arbo.oracoes.domain.repository.PreferencesRepository

class PreferencesDataRepository(private val context: Context) :
    PreferencesRepository {

    private val sharedPreferences by lazy {
        context.getSharedPreferences("default_preferences", Context.MODE_PRIVATE)
    }

    override fun setDailyReminderTime(time: ReminderTime) {
        val editor = sharedPreferences.edit()
        editor.putInt(Keys.DAILY_REMINDER_HOUR.key, time.hour)
        editor.putInt(Keys.DAILY_REMINDER_MINUTE.key, time.minute)
        editor.apply()
    }

    override fun getDailyReminderTime(): ReminderTime {
        return ReminderTime(
            hour = sharedPreferences.getInt(
                Keys.DAILY_REMINDER_HOUR.key,
                DEFAULT_DAILY_REMINDER_HOUR
            ),
            minute = sharedPreferences.getInt(
                Keys.DAILY_REMINDER_MINUTE.key,
                DEFAULT_DAILY_REMINDER_MINUTE
            )
        )
    }

    override fun isReminderEnabled(): Boolean {
        return sharedPreferences.getBoolean(
            Keys.DAILY_REMINDER_ENABLED.key,
            DEFAULT_DAILY_REMINDER_ENABLED
        )
    }

    override fun enabledReminder() {
        val editor = sharedPreferences.edit()
        editor.putBoolean(Keys.DAILY_REMINDER_ENABLED.key, true)
        editor.apply()
    }

    override fun disableReminder() {
        val editor = sharedPreferences.edit()
        editor.putBoolean(Keys.DAILY_REMINDER_ENABLED.key, false)
        editor.apply()
    }

    override fun isDarkThemeEnabled(): Boolean {
        return sharedPreferences.getInt(
            Keys.DARK_THEME_ENABLED.key, DEFAULT_DARK_THEME_ENABLED
        ) == AppCompatDelegate.MODE_NIGHT_YES
    }

    override fun enabledDarkTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        val editor = sharedPreferences.edit()
        editor.putInt(Keys.DARK_THEME_ENABLED.key, AppCompatDelegate.MODE_NIGHT_YES)
        editor.apply()
    }

    override fun disableDarkTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        val editor = sharedPreferences.edit()
        editor.putInt(Keys.DARK_THEME_ENABLED.key, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        editor.apply()
    }

    enum class Keys(val key: String) {
        DAILY_REMINDER_HOUR("daily_reminder_hour"),
        DAILY_REMINDER_MINUTE("daily_reminder_minute"),
        DAILY_REMINDER_ENABLED("daily_reminder_enabled"),
        DARK_THEME_ENABLED("dark_theme_enabled")
    }

    companion object {
        private const val DEFAULT_DAILY_REMINDER_HOUR = 8
        private const val DEFAULT_DAILY_REMINDER_MINUTE = 0
        private const val DEFAULT_DAILY_REMINDER_ENABLED = true

        private const val DEFAULT_DARK_THEME_ENABLED = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }
}