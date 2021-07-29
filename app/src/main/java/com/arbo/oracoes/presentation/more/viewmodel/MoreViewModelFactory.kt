package com.arbo.oracoes.presentation.more.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arbo.oracoes.domain.repository.AnalyticsRepository
import com.arbo.oracoes.domain.repository.PreferencesRepository
import com.arbo.oracoes.util.content.UploadContent
import com.google.firebase.firestore.FirebaseFirestore

class MoreViewModelFactory(
    private val context: Context,
    private val preferencesRepository: PreferencesRepository,
    private val analyticsRepository: AnalyticsRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoreViewModel::class.java)) {
            return MoreViewModel(
                UploadContent(
                    context = context,
                    firestore = FirebaseFirestore.getInstance()
                )
                , preferencesRepository, analyticsRepository
            ) as T
        }
        return super.create(modelClass)
    }

}