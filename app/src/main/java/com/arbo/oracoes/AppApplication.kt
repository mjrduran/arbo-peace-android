package com.arbo.oracoes

import android.app.Application
import android.content.Context
import com.arbo.oracoes.data.di.firebaseModule
import com.arbo.oracoes.data.di.repositoryModule
import com.arbo.oracoes.data.di.useCaseModule
import com.arbo.oracoes.domain.repository.PreferencesRepository
import com.arbo.oracoes.domain.repository.ReminderSchedulerRepository
import com.arbo.oracoes.domain.usecase.SignInAnonymouslyUseCase
import com.arbo.oracoes.presentation.util.extension.TAG
import com.arbo.oracoes.util.AppLogger
import com.crashlytics.android.Crashlytics
import com.google.android.gms.ads.MobileAds
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class AppApplication() : Application(), KodeinAware {

    override val kodein: Kodein by Kodein.lazy {
        bind<Context>() with singleton { this@AppApplication }
        import(firebaseModule)
        import(repositoryModule)
        import(useCaseModule)
    }

    private val signInAnonymouslyUseCase: SignInAnonymouslyUseCase by instance()

    private val preferencesRepository: PreferencesRepository by instance()

    private val reminderSchedulerRepository: ReminderSchedulerRepository by instance()

    override fun onCreate() {
        super.onCreate()

        anonymousLogin()
        scheduleReminder()
        applyDarkMode()
        initAdMob()

        initRemoteConfig()
    }

    private fun initRemoteConfig() {
        val remoteConfig = Firebase.remoteConfig

        if (BuildConfig.DEBUG) {
            val config = remoteConfigSettings {
                minimumFetchIntervalInSeconds = 30
            }
            remoteConfig.setConfigSettingsAsync(config)
        }
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        remoteConfig.fetchAndActivate().addOnSuccessListener {
            AppLogger.d(TAG, "Successfully fetched remote configs")
        }.addOnFailureListener {
            AppLogger.d(TAG, "Error fetching remote configs", it)
            Crashlytics.logException(it)
        }
    }

    private fun initAdMob() {
        MobileAds.initialize(this, getString(R.string.admob_app_id))
    }

    private fun applyDarkMode() {
        if (preferencesRepository.isDarkThemeEnabled()) {
            preferencesRepository.enabledDarkTheme()
        }
    }

    private fun scheduleReminder() {
        if (preferencesRepository.isReminderEnabled()) {
            val reminderTime = preferencesRepository.getDailyReminderTime()
            reminderSchedulerRepository.scheduleDailyReminder(
                reminderTime.hour,
                reminderTime.minute
            )
        }
    }

    private fun anonymousLogin() {
        GlobalScope.launch {
            try {
                signInAnonymouslyUseCase.execute()
            } catch (e: Exception) {
                AppLogger.d(TAG, "Error in anonymous login", e)
            }
        }
    }

}
