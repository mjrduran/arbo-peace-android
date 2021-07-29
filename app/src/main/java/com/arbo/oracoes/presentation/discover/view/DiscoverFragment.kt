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
import com.arbo.oracoes.domain.model.analytics.ClickAudioCategory
import com.arbo.oracoes.domain.repository.AnalyticsRepository
import com.arbo.oracoes.domain.usecase.AudioCategoryFindAllUseCase
import com.arbo.oracoes.presentation.base.view.BaseFragment
import com.arbo.oracoes.presentation.discover.viewmodel.DiscoverViewModel
import com.arbo.oracoes.presentation.discover.viewmodel.DiscoverViewModelFactory
import com.arbo.oracoes.presentation.util.ItemOffsetDecoration
import com.arbo.oracoes.presentation.util.extension.hide
import com.arbo.oracoes.presentation.util.extension.show
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import kotlinx.android.synthetic.main.fragment_audio_discover.*
import org.kodein.di.generic.instance

class DiscoverFragment : BaseFragment() {

    private val audioCategoryFindAllUseCase: AudioCategoryFindAllUseCase by instance()

    private val analyticsRepository: AnalyticsRepository by instance()

    private var categoryClickInterstitial: InterstitialAd? = null

    private val audioCategoryAdapter =
        DiscoverAdapter(discoverClickListener = object : DiscoverClickListener {
            override fun onClick(audioCategory: AudioCategory) {
                displayCategoryClickInterstitial()
                analyticsRepository.trackEvent(ClickAudioCategory(audioCategory.title))
                val bundle = Bundle()
                bundle.putSerializable(AudioListFragment.ARG_AUDIO_CATEGORY, audioCategory)
                findNavController().navigate(R.id.to_audio_list, bundle)
            }
        })

    private val discoverViewModel: DiscoverViewModel by viewModels {
        DiscoverViewModelFactory(audioCategoryFindAllUseCase)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_audio_discover, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupObservers()
        setupRecycler()
        discoverViewModel.loadCategories()
        setupCategoryClickInterstitial()
    }

    private fun setupCategoryClickInterstitial() {
        val activity = activity ?: return
        categoryClickInterstitial = InterstitialAd(activity)
        categoryClickInterstitial?.adUnitId =
            getString(R.string.admob_audio_category_click_interstitial_unid_id)
        categoryClickInterstitial?.loadAd(AdRequest.Builder().build())
        categoryClickInterstitial?.adListener = object : AdListener() {
            override fun onAdClosed() {
                categoryClickInterstitial?.loadAd(AdRequest.Builder().build())
            }
        }
    }

    private fun displayCategoryClickInterstitial() {
        if (categoryClickInterstitial?.isLoaded == true) {
            categoryClickInterstitial?.show()
        }
    }

    private fun setupRecycler() {
        val activity = activity ?: return
        val layoutManger = GridLayoutManager(activity, 2, RecyclerView.VERTICAL, false)
        audio_category_recycler.layoutManager = layoutManger
        audio_category_recycler?.adapter = audioCategoryAdapter

        audio_category_recycler?.addItemDecoration(
            ItemOffsetDecoration(
                activity,
                R.dimen.audio_category_recycler_offset
            )
        )
    }

    private fun setupObservers() {
        discoverViewModel.audioCategories.observe(viewLifecycleOwner, Observer {
            audioCategoryAdapter.update(it)
        })
        discoverViewModel.validationMessage.observe(viewLifecycleOwner, Observer {
            displayValidationMessage(it)
        })
        discoverViewModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            if (loading) {
                audio_categories_progress.show()
                audio_category_recycler.hide()
            } else {
                audio_categories_progress.hide()
                audio_category_recycler.show()
            }
        })
    }
}