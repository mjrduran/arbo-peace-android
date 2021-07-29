package com.arbo.oracoes.presentation.home.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.arbo.oracoes.BuildConfig
import com.arbo.oracoes.R
import com.arbo.oracoes.domain.model.analytics.ClickReminderNotification
import com.arbo.oracoes.domain.repository.AnalyticsRepository
import com.arbo.oracoes.presentation.base.view.BaseActivity
import com.arbo.oracoes.presentation.player.view.AudioPlayerMediaBrowserService
import com.google.android.gms.ads.AdRequest
import com.vorlonsoft.android.rate.AppRate
import com.vorlonsoft.android.rate.StoreType
import com.vorlonsoft.android.rate.Time
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.generic.instance



class MainActivity : BaseActivity() {

    private val analyticsRepository: AnalyticsRepository by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupBottomNavMenu()

        trackNotification()
        loadBannerAd()
        monitoringRateDialog()
    }

    private fun monitoringRateDialog() {
        AppRate.with(this)
            .setStoreType(StoreType.GOOGLEPLAY)
            .setLaunchTimes(3)
            .setRemindTimeToWait(Time.DAY, 2)
            .set365DayPeriodMaxNumberDialogLaunchTimes(6)
            .setDebug(BuildConfig.DEBUG)
            .monitor()
    }

    private fun loadBannerAd() {
        val adRequest = AdRequest.Builder().build()
        bannerAdView?.loadAd(adRequest)
    }

    private fun trackNotification() {
        val fromNotification = intent.getBooleanExtra(EXTRA_FROM_NOTIFICATION, false)
        if (fromNotification){
            analyticsRepository.trackEvent(ClickReminderNotification())
        }
    }

    private fun setupBottomNavMenu() {
        val navController = findNavController(R.id.nav_host_fragment)
        bottom_nav_view?.let {
            NavigationUI.setupWithNavController(it, navController)
        }
        navController.addOnDestinationChangedListener { _, _, _ ->
            val intent = Intent(this, AudioPlayerMediaBrowserService::class.java)
            stopService(intent)
        }
    }

    companion object {
        private const val EXTRA_FROM_NOTIFICATION = "EXTRA_FROM_NOTIFICATION"

        fun newIntent(context: Context, fromNotification: Boolean): Intent{
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(EXTRA_FROM_NOTIFICATION, fromNotification)
            return intent
        }
    }
}
