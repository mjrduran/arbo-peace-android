package com.arbo.oracoes.domain.model.analytics

class ClickDownloadAudio(title: String): Event() {

    override val name = "click_download_audio"

    init {
        addParam(Params.TITLE, title)
    }

    object Params {
        const val TITLE = "title"
    }

}