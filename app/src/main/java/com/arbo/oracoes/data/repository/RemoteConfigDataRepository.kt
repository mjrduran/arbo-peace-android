package com.arbo.oracoes.data.repository

import com.arbo.oracoes.domain.repository.RemoteConfigRepository
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class RemoteConfigDataRepository(private val firebaseRemoteConfig: FirebaseRemoteConfig) :
    RemoteConfigRepository {

    override fun getTextTitleAsNotificationEnabled(): Boolean {
        return firebaseRemoteConfig.getBoolean(TEXT_TITLE_AS_NOTIFICATION_ENABLED)
    }

    companion object {
        const val TEXT_TITLE_AS_NOTIFICATION_ENABLED = "text_title_as_notification_enabled"

    }
}