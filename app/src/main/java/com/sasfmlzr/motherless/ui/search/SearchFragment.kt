package com.sasfmlzr.motherless.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sasfmlzr.motherless.R
import com.sasfmlzr.motherless.data.repository.MotherlessRepository
import com.sasfmlzr.motherless.databinding.FragmentHomeBinding
import com.sasfmlzr.motherless.di.core.FragmentComponent
import com.sasfmlzr.motherless.entity.PreviewData
import com.sasfmlzr.motherless.ui.BaseFragment
import com.sasfmlzr.motherless.util.Converter
import com.sasfmlzr.motherless.view.ErrorScreenView
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.system.measureTimeMillis

class SearchFragment : BaseFragment<SearchViewModel, FragmentHomeBinding>(SearchViewModel::class) {

    @Inject
    lateinit var motherlessRepository: MotherlessRepository

    override fun inject(component: FragmentComponent) = component.inject(this)

    override fun getLayoutId(): Int = R.layout.fragment_search

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

        return binding.root
    }

    override fun setRetryErrorListener() {
        initData()
    }

    override fun initData() {
        initJob = CoroutineScope(Dispatchers.IO + handler + Job()).launch {
            var latestVideos: List<PreviewData> = listOf()

            val measuredSeconds = measureTimeMillis {
                latestVideos = Converter.convertFeedVideosDTOsToPreviewEntity(
                    motherlessRepository.getLatestViewedVideos().take(
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
                dataLayout?.visibility = View.VISIBLE
                errorScreenView?.setState(ErrorScreenView.State.OK)
            }
        }
    }
}
