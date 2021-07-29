package com.arbo.oracoes.domain.model.analytics

class ClickAudioCategory(title: String) : Event() {

    init {
        addParam(Params.TITLE, title)
    }

    override val name = "click_audio_category"

    object Params {
        const val TITLE = "title"
    }

}