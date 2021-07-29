package com.arbo.oracoes.domain.model.analytics

class ShareAppClick(origin: String) : Event() {
    init {
        addParam("origin", origin)
    }
    override val name = "share_app_click"
}