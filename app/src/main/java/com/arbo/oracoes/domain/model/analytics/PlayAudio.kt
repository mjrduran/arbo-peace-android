package com.arbo.oracoes.domain.model.analytics

class PlayAudio(title: String): Event() {

    init {
        addParam(Params.TITLE, title)
    }

    override val name = "play_audio"

    object Params {
        const val TITLE = "title"
    }

}