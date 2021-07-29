package com.arbo.oracoes.presentation.base.view

import android.app.Activity
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import com.arbo.oracoes.R
import com.arbo.oracoes.presentation.base.model.ValidationMessage
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein

open class BaseActivity : AppCompatActivity(), KodeinAware {

    override val kodein by closestKodein()

    protected fun displayValidationMessage(
        validationMessage: ValidationMessage
    ) {
        MaterialAlertDialogBuilder(this)
            .setTitle(validationMessage.titleRes)
            .setMessage(validationMessage.messageRes)
            .setPositiveButton(R.string.generic_ok) { _, _: Int ->
                if (validationMessage.endFlow) {
                    if (validationMessage.isSuccess) {
                        setResult(Activity.RESULT_OK)
                    }
                    finish()
                }
            }.show()
    }
}