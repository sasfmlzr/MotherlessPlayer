package com.sasfmlzr.motherless.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.sasfmlzr.motherless.R
import com.sasfmlzr.motherless.data.repository.MotherlessRepository
import com.sasfmlzr.motherless.databinding.FragmentHomeBinding
import com.sasfmlzr.motherless.di.core.FragmentComponent
import com.sasfmlzr.motherless.di.core.Injector
import com.sasfmlzr.motherless.entity.PreviewData
import com.sasfmlzr.motherless.ui.BaseFragment
import com.sasfmlzr.motherless.util.Converter
import com.sasfmlzr.motherless.view.ErrorScreenView
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.system.measureTimeMillis

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>(HomeViewModel::class) {

    @Inject
    lateinit var motherlessRepository: MotherlessRepository

    override fun inject(component: FragmentComponent) = component.inject(this)

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isNetworkPage = true
        inject(Injector.fragmentComponent())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        viewModel.text.observe(this, Observer {
            binding.textHome.text = it
        })

        return binding.root
    }

    override fun setRetryErrorListener() {
        initData()
    }

    override fun initData() {
        initJob = CoroutineScope(Dispatchers.IO + handler).launch {
            var latestVideos: List<PreviewData> = listOf()
            var favoritesVideos: List<PreviewData> = listOf()
            var mostViewedVideos: List<PreviewData> = listOf()
            val measuredSeconds = measureTimeMillis {
                latestVideos = Converter.convertFeedVideosDTOsToPreviewEntity(
                    motherlessRepository.getLatestViewedVideos().take(
                        12
                    )
                )
                favoritesVideos = Converter.convertFeedVideosDTOsToPreviewEntity(
                    motherlessRepository.getFavoritedVideos().take(
                        12
                    )
                )
                mostViewedVideos = Converter.convertFeedVideosDTOsToPreviewEntity(
                    motherlessRepository.getViewedVideos().take(
                        12
                    )
                )
            }

            val delayMeasuredSeconds = 300
            if (measuredSeconds < delayMeasuredSeconds) {
                delay(delayMeasuredSeconds - measuredSeconds)
            }

            withContext(Dispatchers.Main) {
                binding.beingVideos.setItems(
                    latestVideos
                )
                binding.favoritedVideos.setItems(
                    favoritesVideos
                )
                binding.popularVideos.setItems(
                    mostViewedVideos
                )
                dataLayout?.visibility = View.VISIBLE
                errorScreenView?.setState(ErrorScreenView.State.OK)
            }
        }
    }
}
