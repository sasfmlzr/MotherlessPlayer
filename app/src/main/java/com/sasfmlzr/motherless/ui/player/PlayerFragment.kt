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
import com.sasfmlzr.motherless.di.core.Injector
import com.sasfmlzr.motherless.ui.BaseFragment
import com.sasfmlzr.motherless.view.ErrorScreenView
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.*
import javax.inject.Inject

class PlayerFragment :
    BaseFragment<PlayerViewModel, FragmentPlayerBinding>(PlayerViewModel::class) {

    private lateinit var defaultHttpDataSource: DefaultHttpDataSourceFactory

    @Inject
    lateinit var motherlessRepository: MotherlessRepository

    private var isVideoPrepared = false

    override fun inject(component: FragmentComponent) = component.inject(this)

    private lateinit var mediaSource: ProgressiveMediaSource
    override fun getLayoutId(): Int = R.layout.fragment_player

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject(Injector.fragmentComponent())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val agent = Util.getUserAgent(context!!, getString(R.string.app_name))

        defaultHttpDataSource = DefaultHttpDataSourceFactory(agent, null)

        return binding.root
    }


    override fun initData() {
        val url = arguments?.getString("KEY_URL") ?: ""
        parseSite(url)
    }

    fun parseSite(url: String) {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("Caught $exception")
            exception.printStackTrace()
            initJob.cancel()
            CoroutineScope(Dispatchers.Main).launch {
                error_screen.setState(ErrorScreenView.State.ERROR)
                data_layout.visibility = View.GONE
            }
        }
        error_screen.setState(ErrorScreenView.State.RUNNING)
        initJob = CoroutineScope(Dispatchers.Default + handler).launch {

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
                data_layout.visibility = View.VISIBLE
                error_screen.setState(ErrorScreenView.State.OK)
            }

            println()
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        binding.playerView.player = SimpleExoPlayer.Builder(binding.playerView.context).build()

        binding.playerView.setOnFocusChangeListener { _, _ ->
            println("WTF")
        }

        val player = binding.playerView.player as SimpleExoPlayer
        player.addListener(playerListener)

        data_layout.visibility = View.GONE
        error_screen.setRetryOnClickListener {
            initData()
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