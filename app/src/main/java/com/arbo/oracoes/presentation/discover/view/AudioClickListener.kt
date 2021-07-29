package com.arbo.oracoes.presentation.discover.view

import com.arbo.oracoes.domain.model.DownloadedAudio

interface AudioClickListener {

    fun onPlay(downloadedAudio: DownloadedAudio)

    fun onDownload(downloadedAudio: DownloadedAudio)

    fun onDelete(downloadedAudio: DownloadedAudio)
    
}