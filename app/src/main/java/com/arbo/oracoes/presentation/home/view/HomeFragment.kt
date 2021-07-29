package com.arbo.oracoes.presentation.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.arbo.oracoes.R
import com.arbo.oracoes.domain.model.DownloadedAudio
import com.arbo.oracoes.domain.model.Text
import com.arbo.oracoes.domain.model.analytics.ClickDailyText
import com.arbo.oracoes.domain.model.analytics.ClickWeeklyAudio
import com.arbo.oracoes.domain.repository.AnalyticsRepository
import com.arbo.oracoes.domain.usecase.DailyTextUseCase
import com.arbo.oracoes.domain.usecase.DownloadAudioUseCase
import com.arbo.oracoes.domain.usecase.SignInAnonymouslyUseCase
import com.arbo.oracoes.domain.usecase.WeeklyAudioUseCase
import com.arbo.oracoes.presentation.base.view.BaseFragment
import com.arbo.oracoes.presentation.home.viewmodel.HomeViewModel
import com.arbo.oracoes.presentation.home.viewmodel.HomeViewModelFactory
import com.arbo.oracoes.presentation.mood.view.TextFragment
import com.arbo.oracoes.presentation.player.view.AudioPlayerActivity
import com.arbo.oracoes.presentation.util.extension.hide
import com.arbo.oracoes.presentation.util.extension.show
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import kotlinx.android.synthetic.main.fragment_home.*
import org.kodein.di.generic.instance

class HomeFragment : BaseFragment() {

    private val dailyTextUseCase: DailyTextUseCase by instance()

    private val weeklyAudioUseCase: WeeklyAudioUseCase by instance()

    private val downloadAudioUseCase: DownloadAudioUseCase by instance()

    private val analyticsRepository: AnalyticsRepository by instance()

    private val signInAnonymouslyUseCase: SignInAnonymouslyUseCase by instance()

    private var dailyTextInterstitial: InterstitialAd? = null

    private var weeklyAudioInterstitial: InterstitialAd? = null

    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(
            dailyTextUseCase,
            weeklyAudioUseCase,
            downloadAudioUseCase,
            signInAnonymouslyUseCase
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupObservers()
        homeViewModel.signInAnonymously()

        setupDailyTextInterstitial()
        setupWeeklyTextInterstitial()
    }

    private fun setupDailyTextInterstitial() {
        val activity = activity ?: return
        dailyTextInterstitial = InterstitialAd(activity)
        dailyTextInterstitial?.adUnitId = getString(R.string.admob_daily_text_interstitial_unit_id)
        dailyTextInterstitial?.loadAd(AdRequest.Builder().build())
        dailyTextInterstitial?.adListener = object : AdListener() {
            override fun onAdClosed() {
                dailyTextInterstitial?.loadAd(AdRequest.Builder().build())
            }
        }
    }

    private fun setupWeeklyTextInterstitial() {
        val activity = activity ?: return
        weeklyAudioInterstitial = InterstitialAd(activity)
        weeklyAudioInterstitial?.adUnitId =
            getString(R.string.admob_weekly_audio_interstitial_unit_id)
        weeklyAudioInterstitial?.loadAd(AdRequest.Builder().build())
        weeklyAudioInterstitial?.adListener = object : AdListener() {
            override fun onAdClosed() {
                weeklyAudioInterstitial?.loadAd(AdRequest.Builder().build())
            }
        }
    }

    private fun setupObservers() {
        homeViewModel.validationMessage.observe(viewLifecycleOwner, Observer {
            displayValidationMessage(it)
        })
        homeViewModel.dailyText.observe(viewLifecycleOwner, Observer {
            bindDailyText(it)
        })
        homeViewModel.isSignedIn.observe(viewLifecycleOwner, Observer {
            if (it) {
                homeViewModel.loadDailyText()
                homeViewModel.loadWeeklyAudio()
            }
        })
        homeViewModel.dailyTextLoading.observe(viewLifecycleOwner, Observer { dailyTextLoading ->
            if (dailyTextLoading) {
                daily_text_group?.hide()
                daily_text_progress?.show()
            } else {
                daily_text_group?.show()
                daily_text_progress?.hide()
            }
        })
        homeViewModel.weeklyAudioLoading.observe(viewLifecycleOwner, Observer { loading ->
            if (loading) {
                weekly_audio_group?.hide()
                weekly_audio_progress?.show()
            } else {
                weekly_audio_group?.show()
                weekly_audio_progress?.hide()
            }
        })
        homeViewModel.weeklyAudio.observe(viewLifecycleOwner, Observer {
            bindWeeklyAudio(it)
        })
        homeViewModel.audioDownload.observe(viewLifecycleOwner, Observer { file ->
            navigateToPlayer(file)
        })
    }

    private fun bindWeeklyAudio(downloadedAudio: DownloadedAudio) {
        weekly_audio_title?.text = downloadedAudio.audio.title
        weekly_audio_image?.let {
            Glide.with(it.context).load(downloadedAudio.audio.image)
                .placeholder(R.drawable.ic_placeholder).into(it)

        }
        weekly_audio_container?.setOnClickListener {
            displayWeeklyAudioInterstitial()
            analyticsRepository.trackEvent(ClickWeeklyAudio(downloadedAudio.audio.title))
            if (downloadedAudio.isDownloaded) {
                navigateToPlayer(downloadedAudio)
            } else {
                homeViewModel.downloadAudio(
                    downloadedAudio.audio
                )
            }
        }
    }

    private fun displayWeeklyAudioInterstitial() {
        if (weeklyAudioInterstitial?.isLoaded == true) {
            weeklyAudioInterstitial?.show()
        }
    }

    private fun navigateToPlayer(downloadedAudio: DownloadedAudio) {
        val bundle = Bundle()
        bundle.putString(AudioPlayerActivity.ARG_MEDIA_PATH, downloadedAudio.file?.absolutePath)
        bundle.putString(AudioPlayerActivity.ARG_MEDIA_TITLE, downloadedAudio.audio.title)
        bundle.putString(AudioPlayerActivity.ARG_IMAGE_PATH, downloadedAudio.audio.image)
        findNavController().navigate(R.id.to_audio_player, bundle)
    }

    private fun bindDailyText(text: Text) {
        daily_text_title?.text = text.title
        daily_text_image?.let {
            Glide.with(it.context).load(text.image).placeholder(R.drawable.ic_placeholder).into(it)
        }
        daily_text_container?.setOnClickListener {
            displayDailyTextInterstitial()
            analyticsRepository.trackEvent(ClickDailyText(text.title))
            val bundle = Bundle()
            bundle.putSerializable(TextFragment.ARG_TEXT, text)
            findNavController().navigate(R.id.to_text_fragment, bundle)
        }
    }

    private fun displayDailyTextInterstitial() {
        if (dailyTextInterstitial?.isLoaded == true) {
            dailyTextInterstitial?.show()
        }
    }
}