package com.arbo.oracoes.domain.model

import android.net.Uri
import java.io.File

class DownloadedAudio(
    val audio: Audio,
    val isDownloaded: Boolean = false,
    val file: File? = null,
    val uri: Uri? = null

) {
    override fun equals(other: Any?): Boolean {
        if (other is DownloadedAudio) {
            return this.audio.id == other.audio.id
        }
        return false
    }

    override fun hashCode(): Int {
        var result = audio.hashCode()
        result = 31 * result + isDownloaded.hashCode()
        return result
    }
}