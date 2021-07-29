package com.arbo.oracoes.presentation.base.model

import androidx.annotation.StringRes

data class ValidationMessage(@StringRes val titleRes: Int,
                             @StringRes val messageRes: Int,
                             val endFlow: Boolean,
                             val isSuccess: Boolean = true)