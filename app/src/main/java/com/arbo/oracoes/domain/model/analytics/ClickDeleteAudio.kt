package com.arbo.oracoes.domain.model.analytics

class ClickDeleteAudio(title: String): Event() {

    override val name = "click_delete_audio"

    init {
        addParam(ClickAudioCategory.Params.TITLE, title)
    }

    object Params {
        const val TITLE = "title"
    }

}