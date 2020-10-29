package com.furkancoskun.haberler.ui.newsDetail

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.furkancoskun.haberler.R
import com.furkancoskun.haberler.data.model.News
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.source.dash.DashMediaSource.Factory
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.custom_controller.*
import kotlinx.android.synthetic.main.fragment_news_detail.*
import kotlinx.android.synthetic.main.item_news.iv_news
import kotlinx.android.synthetic.main.item_news.tv_category
import kotlinx.android.synthetic.main.item_news.tv_title
import java.util.*

class NewsDetailFragment : Fragment(), View.OnClickListener {

    lateinit var news: News
    private val sb = StringBuilder()

    private lateinit var exoPlayer: SimpleExoPlayer
    lateinit var trackSelector: DefaultTrackSelector
    var flagFullScreen = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val newsArgs: String = NewsDetailFragmentArgs.fromBundle(it).newsResponse.toString()
            news = Gson().fromJson(newsArgs, News::class.java)
        }

        tv_category.text = news.category
        tv_title.text = news.title

        if (news.videoUrl != "") {
            btn_rew.setOnClickListener(this)
            btn_pause.setOnClickListener(this)
            btn_play.setOnClickListener(this)
            btn_forward.setOnClickListener(this)
            btn_fullscreen.setOnClickListener(this)

            layout_player.visibility = View.VISIBLE
            iv_news.visibility = View.GONE

            buildExoPlayer(Uri.parse(news.videoUrl))
            exoPlayerListener()
        } else {
            layout_player.visibility = View.GONE
            iv_news.visibility = View.VISIBLE
            Picasso.get().load(news.imageUrl).into(iv_news)
        }

        sb.append("<html><body>")
        for (news in news.body) {
            when {
                news.p != null -> {
                    sb.append("<p>" + news.p + "</p>")
                }
                news.h3 != null -> {
                    sb.append("<h3>" + news.h3 + "</h3>")
                }
                news.image != null -> {
                    sb.append("<img width=\"400\" height=\"250\" src=\"" + news.image + "\"/>")
                }
            }
        }
        sb.append("</body></html>")
        webview.loadDataWithBaseURL(null, sb.toString(), "text/html", "utf-8", null)

    }

    override fun onPause() {
        super.onPause()
        if (layout_player.isVisible) {
            // Stop video
            playerPause()
        }
    }

    override fun onResume() {
        super.onResume()
        if (layout_player.isVisible) {
            // Play video when ready
            playerStart()
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_pause -> {
                // Stop video
                exoPlayer.playWhenReady = false
                // Get playback state
                exoPlayer.playbackState
                btn_pause.visibility = View.GONE
                btn_play.visibility = View.VISIBLE
            }
            R.id.btn_play -> {
                // Play video when ready
                exoPlayer.playWhenReady = true
                // Get playback state
                exoPlayer.playbackState
                btn_pause.visibility = View.VISIBLE
                btn_play.visibility = View.GONE
            }
            R.id.btn_rew -> {
                if (exoPlayer.currentPosition >= 10000) {
                    exoPlayer.seekTo(exoPlayer.currentPosition - 10000)
                }
            }
            R.id.btn_forward -> {
                if ((exoPlayer.duration - exoPlayer.currentPosition) >= 10000) {
                    exoPlayer.seekTo(exoPlayer.currentPosition + 10000)
                }
            }
            R.id.btn_fullscreen -> {
                // Check condition
                if (flagFullScreen) {
                    screenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                } else {
                    screenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
                }
            }
        }
    }

    private fun buildExoPlayer(videoURL: Uri) {
        // Init load control
        val loadControl: LoadControl = DefaultLoadControl()
        // Init band width meter
        val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter()
        // Init track selector
        trackSelector = DefaultTrackSelector(AdaptiveTrackSelection.Factory(bandwidthMeter))
        // Init simple exo player
        exoPlayer =
            ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl)
        // Init media source
        val mediaSource: MediaSource = buildMediaSource(videoURL)

        // Set player
        player_view.player = exoPlayer
        // Keep screen on
        player_view.keepScreenOn = true
        // Prepare media
        exoPlayer.prepare(mediaSource)

        // Play video when ready
        exoPlayer.playWhenReady = true
    }

    private fun buildMediaSource(videoUrl: Uri): MediaSource {
        val userAgent = "exoplayer_video"
        val defaultHttpDataSourceFactory = DefaultHttpDataSourceFactory(userAgent)
        return if (videoUrl.lastPathSegment!!.contains("mp3") || videoUrl.lastPathSegment!!.contains(
                "mp4"
            )
        ) {
            ExtractorMediaSource.Factory(defaultHttpDataSourceFactory)
                .createMediaSource(videoUrl)
        } else if (videoUrl.lastPathSegment!!.contains("m3u8")) {
            // Hls
            HlsMediaSource.Factory(defaultHttpDataSourceFactory).createMediaSource(videoUrl)
        } else {
            // Dash
            val dataSourceFactory = DefaultHttpDataSourceFactory("ua", DefaultBandwidthMeter())
            val dashChunkSourceFactory = DefaultDashChunkSource.Factory(dataSourceFactory)
            Factory(dashChunkSourceFactory, dataSourceFactory).createMediaSource(videoUrl)
        }
    }

    // Player listener
    private fun exoPlayerListener() {
        exoPlayer.addListener(object : EventListener, Player.EventListener {
            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {}
            override fun onSeekProcessed() {}
            override fun onTracksChanged(
                trackGroups: TrackGroupArray?,
                trackSelections: TrackSelectionArray?
            ) {
            }

            override fun onLoadingChanged(isLoading: Boolean) {}
            override fun onPositionDiscontinuity(reason: Int) {}
            override fun onRepeatModeChanged(repeatMode: Int) {}
            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {}
            override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {}
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                // Check condition
                when (playbackState) {
                    Player.STATE_BUFFERING -> {
                        // When buffering
                        // Show progress bar
                        progress_bar.visibility = View.VISIBLE
                    }
                    Player.STATE_READY -> {
                        // When buffering
                        // Hide progress bar
                        progress_bar.visibility = View.GONE
                    }
                    Player.STATE_IDLE -> {
                        Log.d("TAG", "onPlayerStateChanged - STATE_IDLE")
                    }
                    Player.STATE_ENDED -> {
                        Log.d("TAG", "onPlayerStateChanged - STATE_ENDED")
                        btn_pause.visibility = View.GONE
                        btn_play.visibility = View.VISIBLE
                        exoPlayer.seekTo(0)
                        playerPause()
                    }
                }
            }

            override fun onPlayerError(error: ExoPlaybackException?) {
                if (error != null) {
                    when (error.type) {
                        ExoPlaybackException.TYPE_RENDERER -> {
                        }
                        ExoPlaybackException.TYPE_SOURCE -> {
                        }
                        ExoPlaybackException.TYPE_UNEXPECTED -> {
                        }
                        else -> {
                        }
                    }
                }
            }
        })
    }

    // Stop video
    private fun playerPause() {
        // Stop video
        exoPlayer.playWhenReady = false
        // Get playback state
        exoPlayer.playbackState
    }

    // Play video
    private fun playerStart() {
        // Play video when ready
        exoPlayer.playWhenReady = true
        // Get playback state
        exoPlayer.playbackState
    }

    // Change screen orientation
    private fun screenOrientation(orientation: Int) {
        if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            // Set enter full screen image
            btn_fullscreen.setImageDrawable(context?.let {
                ContextCompat.getDrawable(
                    it,
                    R.drawable.ic_fullscreen
                )
            })
            // Set portrait orientation
            activity!!.requestedOrientation = orientation
            // Set flag value is false
            flagFullScreen = false
        } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            // Set enter full screen image
            btn_fullscreen.setImageDrawable(context?.let {
                ContextCompat.getDrawable(
                    it,
                    R.drawable.ic_fullscreen_exit
                )
            })
            // Set portrait orientation
            activity!!.requestedOrientation = orientation
            // Set flag value is true
            flagFullScreen = true

        }
    }

}

