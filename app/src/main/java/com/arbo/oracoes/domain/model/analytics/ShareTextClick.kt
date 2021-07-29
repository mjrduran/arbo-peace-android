package com.arbo.oracoes.domain.model.analytics

class ShareTextClick(title: String) : Event() {
    init {
        addParam("title", title)
    }
    override val name = "share_text_click"
}