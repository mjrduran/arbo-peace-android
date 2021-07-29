package com.arbo.oracoes.domain.model.analytics

class ClickDailyText(title: String): Event() {

    init {
        addParam(Params.TITLE, title)
    }

    override val name = "click_daily_text"

    object Params {
        const val TITLE = "title"
    }

}