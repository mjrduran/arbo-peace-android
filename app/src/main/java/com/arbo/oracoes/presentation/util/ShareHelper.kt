package com.arbo.oracoes.presentation.util

import android.content.Context
import android.content.Intent
import com.arbo.oracoes.R
import com.arbo.oracoes.domain.model.analytics.ShareAppClick
import com.arbo.oracoes.domain.repository.AnalyticsRepository

object ShareHelper {

    fun shareAppLink(
        context: Context,
        origin: String,
        analyticsRepository: AnalyticsRepository
    ): Intent {
        val subject = context.getString(R.string.share_app_title)
        val url = context.getString(R.string.share_app_url)
        val text = context.getString(R.string.share_app_text, url)

        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.type = "text/plain"
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        sendIntent.putExtra(Intent.EXTRA_TEXT, text)

        analyticsRepository.trackEvent(ShareAppClick(origin))

        return Intent.createChooser(sendIntent, subject)
    }

    fun createShareContentIntent(subject: String, text: String): Intent {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.type = "text/plain"
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        sendIntent.putExtra(Intent.EXTRA_TEXT, text)
        return Intent.createChooser(sendIntent, subject)
    }
}