package com.arbo.oracoes.data.repository

import android.os.Bundle
import com.arbo.oracoes.BuildConfig
import com.arbo.oracoes.domain.model.analytics.Event
import com.arbo.oracoes.domain.repository.AnalyticsRepository
import com.arbo.oracoes.presentation.util.extension.TAG
import com.arbo.oracoes.util.AppLogger

import com.google.firebase.analytics.FirebaseAnalytics

class AnalyticsDataRepository(val firebaseAnalytics: FirebaseAnalytics) : AnalyticsRepository {

    override fun trackEvent(event: Event) {
        val bundle = Bundle()
        event.params.entries.forEach { entry ->
            bundle.putString(entry.key, entry.value)
        }
        logEvent(event, bundle)
    }

    private fun logEvent(event: Event, bundle: Bundle) {
        if (BuildConfig.DEBUG) {
            AppLogger.v(TAG, event.toString())
        } else {
            firebaseAnalytics.logEvent(event.name, bundle)
        }
    }
}