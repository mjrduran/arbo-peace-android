package com.arbo.oracoes.presentation.discover.view

import com.arbo.oracoes.domain.model.AudioCategory

interface DiscoverClickListener {

    fun onClick(audioCategory: AudioCategory)

}