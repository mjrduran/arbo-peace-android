package com.arbo.oracoes.presentation.player.view

import android.content.ComponentName
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.fragment.app.FragmentActivity
import com.arbo.oracoes.R
import com.arbo.oracoes.domain.model.analytics.AudioDontRateEvent
import com.arbo.oracoes.domain.model.analytics.AudioRateEvent
import com.arbo.oracoes.domain.model.analytics.AudioRateLaterEvent
import com.arbo.oracoes.domain.repository.AnalyticsRepository
import com.arbo.oracoes.presentation.base.view.BaseActivity
import com.arbo.oracoes.presentation.util.extension.hide
import com.arbo.oracoes.presentation.util.extension.show
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.util.Util
import com.vorlonsoft.android.rate.AppRate
import kotlinx.android.synthetic.main.activity_audio_player.*
import org.kodein.di.generic.instance

class AudioPlayerActivity : BaseActivity() {

    private lateinit var mediaBrowser: MediaBrowserCompat
    private val analyticsRepository: AnalyticsRepository by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        initMediaBrowser()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        stopService()
    }

    private fun initMediaBrowser() {
        val activity = this
        mediaBrowser = MediaBrowserCompat(
            activity,
            ComponentName(activity, AudioPlayerMediaBrowserService::class.java),
            object : MediaBrowserCompat.ConnectionCallback() {
                override fun onConnected() {

                    mediaBrowser.sessionToken.also { token ->
                        val mediaController = MediaControllerCompat(activity, token)
                        MediaControllerCompat.setMediaController(activity, mediaController)
                        buildTransportControls()
                    }
                }
            },
            null
        )
    }

    private fun buildTransportControls() {
        val activity = this
        val mediaController = MediaControllerCompat.getMediaController(activity)

        val pbState = mediaController.playbackState.state
        updateButtonState(pbState)

        audio_player_play_button?.setOnClickListener {
            mediaController.transportControls.play()
        }

        audio_player_pause_button?.setOnClickListener {
            mediaController.transportControls.pause()
        }

        mediaController.registerCallback(object : MediaControllerCompat.Callback() {
            override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
                updateButtonState(state.state)
            }
        })

    }

    private fun updateButtonState(state: Int) {
        if (state == PlaybackStateCompat.STATE_PLAYING) {
            audio_player_pause_button?.show()
            audio_player_play_button?.hide()
        } else if (state == PlaybackStateCompat.STATE_STOPPED) {
            displayRateDialog()
            audio_player_pause_button?.hide()
            audio_player_play_button?.show()
        } else {
            audio_player_pause_button?.hide()
            audio_player_play_button?.show()
        }
    }

    private fun displayRateDialog() {
        AppRate.with(this).setOnClickButtonListener { which ->
            when (which.toInt()) {
                DialogInterface.BUTTON_POSITIVE -> {
                    analyticsRepository.trackEvent(AudioRateEvent())
                }

                DialogInterface.BUTTON_NEGATIVE -> {
                    analyticsRepository.trackEvent(AudioDontRateEvent())
                }

                DialogInterface.BUTTON_NEUTRAL -> {
                    analyticsRepository.trackEvent(AudioRateLaterEvent())
                }
            }
        }
        AppRate.showRateDialogIfMeetsConditions(this)
    }

    private fun setupListeners() {
        audio_player_close?.setOnClickListener {
            stopService()
            finish()
        }
    }

    private fun stopService() {
        val intent = Intent(this, AudioPlayerMediaBrowserService::class.java)
        stopService(intent)
    }

    private fun initializePlayer() {
        val activity = this
        setupListeners()
        loadArtwork(activity)

        val mediaPath = intent?.getStringExtra(ARG_MEDIA_PATH)
        val mediaTitle = intent?.getStringExtra(ARG_MEDIA_TITLE)
        val intent = Intent(activity, AudioPlayerMediaBrowserService::class.java)
        intent.putExtra(AudioPlayerMediaBrowserService.ARG_MEDIA_PATH, mediaPath)
        intent.putExtra(AudioPlayerMediaBrowserService.ARG_MEDIA_TITLE, mediaTitle)
        Util.startForegroundService(activity, intent)
    }

    private fun loadArtwork(activity: FragmentActivity) {
        val imageUrl = intent?.getStringExtra(ARG_IMAGE_PATH) ?: return
        audio_player_image?.let {
            Glide.with(activity).load(imageUrl).into(it)
        }
    }

    override fun onStart() {
        super.onStart()
        initializePlayer()
        mediaBrowser.connect()
    }

    override fun onStop() {
        super.onStop()
        mediaBrowser.disconnect()
    }

    companion object {
        const val ARG_MEDIA_PATH = "ARG_MEDIA_PATH"
        const val ARG_IMAGE_PATH = "ARG_IMAGE_PATH"
        const val ARG_MEDIA_TITLE = "ARG_MEDIA_TITLE"
    }
}