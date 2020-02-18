package com.sasfmlzr.motherless.ui.player

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.sasfmlzr.motherless.R
import com.sasfmlzr.motherless.data.repository.MotherlessRepository
import com.sasfmlzr.motherless.databinding.FragmentPlayerBinding
import com.sasfmlzr.motherless.di.core.FragmentComponent
import com.sasfmlzr.motherless.ui.BaseFragment
import com.sasfmlzr.motherless.view.ErrorScreenView
import kotlinx.coroutines.*
import javax.inject.Inject

class PlayerFragment :
    BaseFragment<PlayerViewModel, FragmentPlayerBinding>(PlayerViewModel::class) {

    @Inject
    lateinit var motherlessRepository: MotherlessRepository

    override fun inject(component: FragmentComponent) = component.inject(this)

    override fun getLayoutId(): Int = R.layout.fragment_player

    private lateinit var defaultHttpDataSource: DefaultHttpDataSourceFactory
    private lateinit var mediaSource: ProgressiveMediaSource
    private var isVideoPrepared = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isNetworkPage = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val agent = Util.getUserAgent(context!!, getString(R.string.app_name))
        defaultHttpDataSource = DefaultHttpDataSourceFactory(agent, null)
        binding.playerView.player = SimpleExoPlayer.Builder(binding.playerView.context).build()

        val player = binding.playerView.player as SimpleExoPlayer
        player.addListener(playerListener)

        return binding.root
    }

    override fun setRetryErrorListener() {
        initData()
    }

    override fun initData() {
        val url = arguments?.getString("KEY_URL") ?: ""
        parseSite(url)
    }

    private fun parseSite(url: String) {
        initJob = CoroutineScope(Dispatchers.Default + handler + Job()).launch {
            val videoData = motherlessRepository.getVideoData(url)
            withContext(Dispatchers.Main) {
                mediaSource = ProgressiveMediaSource.Factory(defaultHttpDataSource)
                    .createMediaSource(Uri.parse(videoData.urlVideo))
                if (!isVideoPrepared) {
                    prepareVideo()
                }

                binding.nameVideo.text = videoData.title
                binding.dateVideo.text = videoData.dateVideo
                binding.viewedCount.text = videoData.viewedCount
                binding.likeCount.text = videoData.likeCount
                binding.tags.text = "Tags: ${videoData.tags}"
                dataLayout?.visibility = View.VISIBLE
                errorScreenView?.setState(ErrorScreenView.State.OK)
            }
        }
    }

    override fun onStop() {
        binding.playerView.player?.playWhenReady = false
        super.onStop()
    }

    override fun onDestroy() {
        binding.playerView.player?.release()
        super.onDestroy()
    }

    private fun prepareVideo() {
        val player = binding.playerView.player as SimpleExoPlayer
        player.prepare(mediaSource)
        isVideoPrepared = true
    }

    private val playerListener by lazy {
        object : Player.EventListener {

            override fun onPlayerError(error: ExoPlaybackException) {

                if (error.message != "java.lang.IllegalArgumentException") {
                    println("Network error")
                } else {
                    println("Internal error")
                }

                super.onPlayerError(error)
            }
        }
    }
}