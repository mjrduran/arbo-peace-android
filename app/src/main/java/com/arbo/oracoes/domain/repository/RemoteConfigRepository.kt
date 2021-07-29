package com.arbo.oracoes.domain.repository

interface RemoteConfigRepository {

    fun getTextTitleAsNotificationEnabled(): Boolean

}