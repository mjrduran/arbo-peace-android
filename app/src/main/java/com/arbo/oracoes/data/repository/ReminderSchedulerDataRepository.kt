package com.arbo.oracoes.data.repository

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.arbo.oracoes.data.worker.ReminderWorker
import com.arbo.oracoes.domain.repository.ReminderSchedulerRepository
import java.util.*
import java.util.concurrent.TimeUnit

class ReminderSchedulerDataRepository(val context: Context) : ReminderSchedulerRepository {

    override fun scheduleDailyReminder(hour: Int, minute: Int) {
        scheduleDailyReminder(hour, minute, ExistingWorkPolicy.KEEP)
    }

    private fun scheduleDailyReminder(
        hour: Int,
        minute: Int,
        existingWorkPolicy: ExistingWorkPolicy
    ) {

        val workManager = WorkManager.getInstance(context)
        val initialDelay = getInitialDelay(hour, minute)

        val dataBuilder = Data.Builder()
        dataBuilder.putInt(ReminderWorker.DATA_HOUR, hour)
        dataBuilder.putInt(ReminderWorker.DATA_MINUTE, minute)

        val dailyWorkRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setInputData(dataBuilder.build())
            .addTag(ReminderWorker.REMINDER_TAG)
            .build()

        workManager.enqueueUniqueWork(
            ReminderWorker.UNIQUE_WORK_NAME,
            existingWorkPolicy,
            dailyWorkRequest
        )
    }

    override fun reScheduleDailyReminder(hour: Int, minute: Int) {
        scheduleDailyReminder(hour, minute, ExistingWorkPolicy.REPLACE)
    }

    private fun getInitialDelay(hour: Int, minute: Int): Long {
        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()

        dueDate.set(Calendar.HOUR_OF_DAY, hour)
        dueDate.set(Calendar.MINUTE, minute)
        dueDate.set(Calendar.SECOND, 0)
        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24)
        }
        return dueDate.timeInMillis - currentDate.timeInMillis
    }

    override fun cancelDailyReminder() {
        val workManager = WorkManager.getInstance(context)
        workManager.cancelAllWorkByTag(ReminderWorker.REMINDER_TAG)
    }
}