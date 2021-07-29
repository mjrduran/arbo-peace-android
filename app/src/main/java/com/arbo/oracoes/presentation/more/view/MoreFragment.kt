package com.arbo.oracoes.presentation.more.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.arbo.oracoes.R
import com.arbo.oracoes.domain.repository.AnalyticsRepository
import com.arbo.oracoes.domain.repository.PreferencesRepository
import com.arbo.oracoes.presentation.base.view.BaseFragment
import com.arbo.oracoes.presentation.more.viewmodel.MoreViewModel
import com.arbo.oracoes.presentation.more.viewmodel.MoreViewModelFactory
import com.arbo.oracoes.presentation.util.ShareHelper
import kotlinx.android.synthetic.main.fragment_more.*
import org.kodein.di.generic.instance

class MoreFragment : BaseFragment() {

    private val preferencesRepository: PreferencesRepository by instance()

    private val analyticsRepository: AnalyticsRepository by instance()

    private val moreViewModel: MoreViewModel by viewModels {
        MoreViewModelFactory(context!!, preferencesRepository, analyticsRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_more, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupListeners()
        setupObservers()
        moreViewModel.loadDarkModeState()
    }

    private fun setupObservers() {
        moreViewModel.validationMessage.observe(viewLifecycleOwner, Observer {
            displayValidationMessage(it)
        })
        moreViewModel.isDarkThemeEnable.observe(viewLifecycleOwner, Observer {
            dark_theme_switch?.isChecked = it

            dark_theme_switch?.setOnCheckedChangeListener { button, checked ->
                moreViewModel.enableOrDisableDarkTheme(checked)
            }
        })
    }

    private fun setupListeners() {
        val activity = activity ?: return

        more_item_reminder?.setOnClickListener {
            findNavController().navigate(R.id.action_more_to_reminderFragment)
        }
        more_item_privacy_policy?.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW)
            browserIntent.data = Uri.parse(PRIVACY_POLICY_URL)
            startActivity(browserIntent)
        }

        more_item_share?.setOnClickListener {
            startActivity(ShareHelper.shareAppLink(activity, "more", analyticsRepository))
        }
    }



    companion object {
        private const val PRIVACY_POLICY_URL =
            "https://storage.googleapis.com/arbocorp/oracoes_privacy_policy.html"
    }
}