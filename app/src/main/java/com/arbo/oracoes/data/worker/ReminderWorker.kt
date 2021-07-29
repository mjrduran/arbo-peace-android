package com.arbo.oracoes.data.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.arbo.oracoes.R
import com.arbo.oracoes.data.repository.ReminderSchedulerDataRepository
import com.arbo.oracoes.domain.repository.RemoteConfigRepository
import com.arbo.oracoes.domain.repository.TextRepository
import com.arbo.oracoes.domain.usecase.DailyTextUseCase
import com.arbo.oracoes.presentation.home.view.MainActivity
import com.arbo.oracoes.presentation.util.extension.TAG
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class ReminderWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters), KodeinAware {

    override val kodein by closestKodein(context)

    private val remoteConfigRepository: RemoteConfigRepository by instance()

    private val textRepository: TextRepository by instance()

    private val reminderSchedulerRepository: ReminderSchedulerDataRepository by instance()


    override fun doWork(): Result {
        return try {
            val context = applicationContext

            GlobalScope.launch {
                val dailyText = textRepository.getDailyText()

                val message = if (remoteConfigRepository.getTextTitleAsNotificationEnabled()) {
                    dailyText.title
                } else {
                    context.getString(R.string.daily_notification_message)
                }
                val title = if (remoteConfigRepository.getTextTitleAsNotificationEnabled()) {
                    context.getString(R.string.daily_notification_text_title)
                } else {
                    context.getString(R.string.daily_notification_title)
                }

                makeStatusNotification(
                    message,
                    title,
                    context
                )
            }

            val hour = inputData.getInt(DATA_HOUR, 0)
            val minute = inputData.getInt(DATA_MINUTE, 0)
            reminderSchedulerRepository.reScheduleDailyReminder(hour, minute)

            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Error scheduling notification",e)
            Result.failure()
        }
    }

    private fun makeStatusNotification(message: String, title: String, context: Context) {
        createNotificationChannel(context)

        val intent = MainActivity.newIntent(context, true)

        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(
            context,
            CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_reminder_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(LongArray(0))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.notification_channel_name)
            val description = context.getString(R.string.notification_channel_description)

            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

            notificationManager?.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "DEFAULT_NOTIFICATIONS"
        private const val NOTIFICATION_ID = 4545

        const val DATA_HOUR = "REMINDER_HOUR"
        const val DATA_MINUTE = "REMINDER_MINUTE"
        const val REMINDER_TAG = "REMINDER_TAG"
        const val UNIQUE_WORK_NAME = "REMINDER_WORK"
    }

}