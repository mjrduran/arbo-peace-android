package com.arbo.oracoes.presentation.base.view


import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.arbo.oracoes.R
import com.arbo.oracoes.presentation.base.model.ValidationMessage
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

open class BaseFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()

    protected fun displayValidationMessage(
        validationMessage: ValidationMessage
    ) {
        MaterialAlertDialogBuilder(activity)
            .setTitle(validationMessage.titleRes)
            .setMessage(validationMessage.messageRes)
            .setPositiveButton(R.string.generic_ok) { _, _: Int ->
                if (validationMessage.endFlow) {
                    findNavController().popBackStack()
                }
            }.show()
    }

}