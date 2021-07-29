package com.arbo.oracoes.presentation.discover.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arbo.oracoes.R
import com.arbo.oracoes.domain.model.AudioCategory
import com.arbo.oracoes.domain.model.DownloadedAudio
import com.arbo.oracoes.domain.model.analytics.ClickDeleteAudio
import com.arbo.oracoes.domain.model.analytics.ClickDownloadAudio
import com.arbo.oracoes.domain.model.analytics.PlayAudio
import com.arbo.oracoes.domain.repository.AnalyticsRepository
import com.arbo.oracoes.domain.usecase.AudioFindByIdsUseCase
import com.arbo.oracoes.domain.usecase.DownloadAudioUseCase
import com.arbo.oracoes.presentation.base.view.BaseFragment
import com.arbo.oracoes.presentation.discover.viewmodel.AudioListViewModel
import com.arbo.oracoes.presentation.discover.viewmodel.AudioListViewModelFactory
import com.arbo.oracoes.presentation.player.view.AudioPlayerActivity
import com.arbo.oracoes.presentation.util.ItemOffsetDecoration
import com.arbo.oracoes.presentation.util.extension.hide
import com.arbo.oracoes.presentation.util.extension.show
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import kotlinx.android.synthetic.main.fragment_audio_list.*
import org.kodein.di.generic.instance

class AudioListFragment : BaseFragment() {

    private val audioFindByIdsUseCase: AudioFindByIdsUseCase by instance()
    private val downloadAudioUseCase: DownloadAudioUseCase by instance()

    private val analyticsRepository: AnalyticsRepository by instance()

    private var playAudioInterstitial: InterstitialAd? = null

    private val audioListViewModel: AudioListViewModel by viewModels {
        AudioListViewModelFactory(audioFindByIdsUseCase, downloadAudioUseCase)
    }

    private val audioCategoryAdapter =
        AudioListAdapter(audioClickListener = object : AudioClickListener {
            override fun onPlay(downloadedAudio: DownloadedAudio) {
                analyticsRepository.trackEvent(PlayAudio(downloadedAudio.audio.title))
                if (downloadedAudio.isDownloaded) {
                    navigateToOfflinePlayer(downloadedAudio)
                } else {
                    navigateToOnlinePlayer(downloadedAudio)
                }
            }

            override fun onDownload(downloadedAudio: DownloadedAudio) {
                analyticsRepository.trackEvent(ClickDownloadAudio(downloadedAudio.audio.title))
                audioListViewModel.downloadAudio(
                    downloadedAudio.audio
                )
            }

            override fun onDelete(downloadedAudio: DownloadedAudio) {
                analyticsRepository.trackEvent(ClickDeleteAudio(downloadedAudio.audio.title))
                if (downloadedAudio.isDownloaded) {
                    audioListViewModel.deleteAudio(downloadedAudio)
                }
            }
        })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_audio_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupObservers()
        setupRecycler()
        loadAudioList()
        setupListeners()
        setupPlayAudioInterstitial()
    }

    private fun setupPlayAudioInterstitial() {
        val activity = activity ?: return
        playAudioInterstitial = InterstitialAd(activity)
        playAudioInterstitial?.adUnitId =
            getString(R.string.admob_play_audio_interstitial_unid_id)
        playAudioInterstitial?.loadAd(AdRequest.Builder().build())
        playAudioInterstitial?.adListener = object : AdListener() {
            override fun onAdClosed() {
                playAudioInterstitial?.loadAd(AdRequest.Builder().build())
            }
        }
    }

    private fun setupListeners() {
        back_button?.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun loadAudioList() {
        val audioCategory = arguments?.getSerializable(ARG_AUDIO_CATEGORY) as AudioCategory?
        audioCategory?.let {
            bind(it)
        }
    }

    private fun bind(audioCategory: AudioCategory) {
        audioListViewModel.loadAudios(audioCategory.audios)
        audio_title?.text = audioCategory.title
    }

    private fun setupRecycler() {
        val activity = activity ?: return
        val layoutManger = GridLayoutManager(activity, 2, RecyclerView.VERTICAL, false)
        audios_list_recycler?.layoutManager = layoutManger
        audios_list_recycler?.adapter = audioCategoryAdapter
        audios_list_recycler?.addItemDecoration(
            ItemOffsetDecoration(
                activity,
                R.dimen.audio_list_recycler_offset
            )
        )
    }

    private fun setupObservers() {
        audioListViewModel.audios.observe(viewLifecycleOwner, Observer {
            audioCategoryAdapter.update(it.toMutableList())
            audioListViewModel.updateAudiosDownloadStatus()
        })
        audioListViewModel.validationMessage.observe(viewLifecycleOwner, Observer {
            displayValidationMessage(it)
        })
        audioListViewModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            if (loading) {
                audio_list_progress.show()
                audios_list_recycler.hide()
            } else {
                audio_list_progress.hide()
                audios_list_recycler.show()
            }
        })
        audioListViewModel.audioDownload.observe(viewLifecycleOwner, Observer { downloadedAudio ->
            audioCategoryAdapter.updateItem(downloadedAudio)
        })
        audioListViewModel.audioDelete.observe(viewLifecycleOwner, Observer { downloadedAudio ->
            audioCategoryAdapter.updateItem(downloadedAudio)
        })
        audioListViewModel.audioWithDownloadStatus.observe(
            viewLifecycleOwner,
            Observer { downloadedAudio ->
                audioCategoryAdapter.updateItem(downloadedAudio)
            })
    }

    private fun navigateToOfflinePlayer(downloadedAudio: DownloadedAudio) {
        showPlayAudioInterstitial()
        val bundle = Bundle()

        if (downloadedAudio.file != null) {
            bundle.putString(AudioPlayerActivity.ARG_MEDIA_PATH, downloadedAudio.file?.absolutePath)
            bundle.putString(AudioPlayerActivity.ARG_MEDIA_TITLE, downloadedAudio.audio.title)
            bundle.putString(AudioPlayerActivity.ARG_IMAGE_PATH, downloadedAudio.audio.image)
            findNavController().navigate(R.id.to_audio_player, bundle)
        }
    }

    private fun navigateToOnlinePlayer(downloadedAudio: DownloadedAudio) {
        showPlayAudioInterstitial()
        val bundle = Bundle()
        if (downloadedAudio.uri != null) {
            bundle.putString(AudioPlayerActivity.ARG_MEDIA_PATH, downloadedAudio.uri?.toString())
            bundle.putString(AudioPlayerActivity.ARG_MEDIA_TITLE, downloadedAudio.audio.title)
            bundle.putString(AudioPlayerActivity.ARG_IMAGE_PATH, downloadedAudio.audio.image)
            findNavController().navigate(R.id.to_audio_player, bundle)
        }
    }

    private fun showPlayAudioInterstitial() {
        if (playAudioInterstitial?.isLoaded == true) {
            playAudioInterstitial?.show()
        }
    }

    companion object {
        const val ARG_AUDIO_CATEGORY = "ARG_AUDIO_CATEGORY"
    }
}