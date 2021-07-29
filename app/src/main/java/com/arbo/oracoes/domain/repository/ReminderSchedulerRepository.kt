package com.arbo.oracoes.domain.repository

interface ReminderSchedulerRepository {

    fun scheduleDailyReminder(hour: Int, minute: Int)

    fun reScheduleDailyReminder(hour: Int, minute: Int)

    fun cancelDailyReminder()

}