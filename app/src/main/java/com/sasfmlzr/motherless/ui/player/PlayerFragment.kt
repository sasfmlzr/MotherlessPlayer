package com.sasfmlzr.motherless.ui.player

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.sasfmlzr.motherless.R
import com.sasfmlzr.motherless.data.repository.MotherlessRepository
import com.sasfmlzr.motherless.databinding.FragmentPlayerBinding
import com.sasfmlzr.motherless.di.core.FragmentComponent
import com.sasfmlzr.motherless.di.core.Injector
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import javax.inject.Inject

class PlayerFragment : Fragment() {

    private lateinit var playerViewModel: PlayerViewModel
    private lateinit var defaultHttpDataSource: DefaultHttpDataSourceFactory

    @Inject
    lateinit var motherlessRepository: MotherlessRepository

    private var isVideoPrepared = false

    fun inject(component: FragmentComponent) = component.inject(this)

    private lateinit var binding: FragmentPlayerBinding
    private lateinit var mediaSource: ProgressiveMediaSource
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject(Injector.fragmentComponent())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_player, container, false)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_player,
            container,
            false
        )
        playerViewModel =
            ViewModelProviders.of(this).get(PlayerViewModel::class.java)

        val url = arguments?.getString("KEY_URL") ?: ""

        parseSite(url)

        val agent = Util.getUserAgent(context!!, getString(R.string.app_name))

        defaultHttpDataSource = DefaultHttpDataSourceFactory(agent, null)

        return binding.root
    }

    fun parseSite(url: String) {

        CoroutineScope(Dispatchers.Default).launch {
            val doc = Jsoup.connect(url).get().body()

            val urlVideo = doc.getElementById("content").getElementsByTag("script").filter {
                it.attr("type") == "text/javascript"
            }[0].dataNodes()[0].wholeData.split("__fileurl = '")[1].split("';")[0]

            val title =
                doc.getElementById("media-info")
                    .getElementsByClass("media-meta-info")
                    .first()
                    .getElementsByTag("h1")
                    .first()
                    .textNodes()
                    .first()
                    .text()

            val videoDetails = doc.getElementById("media-info")
                .getElementsByClass("media-meta-info")
                .first()
                .getElementsByTag("h2").map {
                    val key = it.children().first().textNodes().first().text()
                    val value = it.textNodes().last().text()
                    Pair(key, value)
                }

            val tags = doc.getElementById("media-tags-container")
                .children()
                .map {
                    it.getElementsByTag("h4")
                }
                .flatten()
                .map {
                    it.children().text()
                }

            withContext(Dispatchers.Main) {
                mediaSource = ProgressiveMediaSource.Factory(defaultHttpDataSource)
                    .createMediaSource(Uri.parse(urlVideo))
                if (!isVideoPrepared) {
                    prepareVideo()
                }

                binding.nameVideo.text = title
                binding.dateVideo.text = videoDetails[0].second
                binding.viewedCount.text = videoDetails[1].second
                binding.likeCount.text = videoDetails[2].second

                binding.tags.text = "Tags: $tags"
            }

            println()
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.playerView.player = SimpleExoPlayer.Builder(binding.playerView.context).build()

        binding.playerView.setOnFocusChangeListener { _, _ ->
            println("WTF")
        }

        val player = binding.playerView.player as SimpleExoPlayer
        player.addListener(playerListener)

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