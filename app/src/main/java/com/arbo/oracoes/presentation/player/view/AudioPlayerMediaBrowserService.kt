package com.arbo.oracoes.presentation.player.view

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.annotation.Nullable
import androidx.media.MediaBrowserServiceCompat
import com.arbo.oracoes.R
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.ui.PlayerNotificationManager.BitmapCallback
import com.google.android.exoplayer2.ui.PlayerNotificationManager.MediaDescriptionAdapter
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory

internal class AudioPlayerMediaBrowserService : MediaBrowserServiceCompat() {

    private var player: SimpleExoPlayer? = null
    private var playerNotificationManager: PlayerNotificationManager? = null
    private var mediaSession: MediaSessionCompat? = null
    private var mediaSessionConnector: MediaSessionConnector? = null

    private var mediaPath: String? = null
    private var mediaTitle: String? = ""
    private var mediaDescription: String? = ""

    override fun onCreate() {
        super.onCreate()
        initMediaSession()
        initPlayer()
    }

    private fun initMediaSession() {
        val mediaSession = MediaSessionCompat(this, MEDIA_SESSION_TAG)
        this.mediaSession = mediaSession
        sessionToken = mediaSession.sessionToken
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        result.sendResult(null)
        return
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        return BrowserRoot(EMPTY_ROOT_ID, null)
    }

    private fun initPlayer() {
        val context: Context = this
        val player = ExoPlayerFactory.newSimpleInstance(context)
        this.player = player
        player.playWhenReady = true

        val playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
            context,
            PLAYBACK_CHANNEL_ID,
            R.string.playback_channel_name,
            R.string.playback_channel_description,
            PLAYBACK_NOTIFICATION_ID,
            object : MediaDescriptionAdapter {
                override fun getCurrentContentTitle(player: Player): String? {
                    return mediaTitle
                }

                @Nullable
                override fun createCurrentContentIntent(player: Player): PendingIntent? {
                    return null
                }

                @Nullable
                override fun getCurrentContentText(player: Player): String? {
                    return mediaDescription
                }

                @Nullable
                override fun getCurrentLargeIcon(
                    player: Player,
                    callback: BitmapCallback
                ): Bitmap? {
                    return null
                }
            },
            object :
                PlayerNotificationManager.NotificationListener {
                override fun onNotificationStarted(
                    notificationId: Int,
                    notification: Notification
                ) {
                    startForeground(notificationId, notification)
                }

                override fun onNotificationCancelled(notificationId: Int) {
                    stopForeground(true)
                }
            }
        )
        this.playerNotificationManager = playerNotificationManager
        playerNotificationManager.setUseStopAction(true)

        playerNotificationManager.setPlayer(player)
        playerNotificationManager.setMediaSessionToken(sessionToken)


        val mediaSessionConnector = MediaSessionConnector(mediaSession)
        this.mediaSessionConnector = mediaSessionConnector
        mediaSessionConnector.setPlayer(player)
    }

    private fun buildMediaSource(uri: Uri): MediaSource? {
        val dataSourceFactory: DataSource.Factory =
            DefaultDataSourceFactory(this, "exoplayer")
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)
    }

    override fun onDestroy() {
        mediaSession?.release()
        mediaSessionConnector?.setPlayer(null)
        playerNotificationManager?.setPlayer(null)
        player?.release()
        player = null
        super.onDestroy()
    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        mediaPath = intent.getStringExtra(ARG_MEDIA_PATH)
        mediaTitle = intent.getStringExtra(ARG_MEDIA_TITLE)
        mediaDescription = intent.getStringExtra(ARG_MEDIA_DESCRIPTION)

        val uri = Uri.parse(mediaPath)
        val mediaSource = buildMediaSource(uri)
        player?.prepare(mediaSource, false, false)
        return Service.START_NOT_STICKY
    }

    companion object {
        const val PLAYBACK_CHANNEL_ID = "DEFAULT"
        const val PLAYBACK_NOTIFICATION_ID = 1
        const val MEDIA_SESSION_TAG = "default_media_session"

        const val ARG_MEDIA_PATH = "ARG_MEDIA_PATH"
        const val ARG_MEDIA_TITLE = "ARG_MEDIA_TITLE"
        const val ARG_MEDIA_DESCRIPTION = "ARG_MEDIA_DESCRIPTION"

        const val EMPTY_ROOT_ID = "EMPTY_ROOT"
    }
}