package com.arbo.oracoes.domain.model.analytics

class ClickWeeklyAudio(title: String): Event() {

    init {
        addParam(Params.TITLE, title)
    }

    override val name = "click_weekly_audio"

    object Params {
        const val TITLE = "title"
    }

}