package com.arbo.oracoes.domain.repository

import android.net.Uri
import java.io.File

interface StorageRepository {

    suspend fun downloadAudioFile(fileName: String, storageUrl: String): File

    suspend fun getFile(fileName: String): File

    suspend fun getDownloadUrl(storageUrl: String): Uri
}