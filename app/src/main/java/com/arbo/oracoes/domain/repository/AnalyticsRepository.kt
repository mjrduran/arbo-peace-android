package com.arbo.oracoes.domain.repository

import com.arbo.oracoes.domain.model.analytics.Event

interface AnalyticsRepository {

    fun trackEvent(event: Event)
}