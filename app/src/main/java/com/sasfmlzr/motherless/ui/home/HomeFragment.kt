package com.sasfmlzr.motherless.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sasfmlzr.motherless.R
import com.sasfmlzr.motherless.data.repository.MotherlessRepository
import com.sasfmlzr.motherless.di.core.FragmentComponent
import com.sasfmlzr.motherless.di.core.Injector
import com.sasfmlzr.motherless.util.Converter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.*
import javax.inject.Inject

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var motherlessRepository: MotherlessRepository

    fun inject(component: FragmentComponent) = component.inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject(Injector.fragmentComponent())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(this, Observer {
            textView.text = it
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        being_videos.setItems()
        popular_videos.setItems()
        favorited_videos.setItems()

        val handler = CoroutineExceptionHandler { _, exception ->
            println("Caught $exception")
            throw exception
        }

        GlobalScope.launch(handler) {
            val recentVideos = Converter.convertFeedVideosDTOsToPreviewEntity(
                motherlessRepository.getRecentVideos().take(
                    12
                )
            )
            val favoritesVideos = Converter.convertFeedVideosDTOsToPreviewEntity(
                motherlessRepository.getFavoritedVideos().take(
                    12
                )
            )
            val mostViewedVideos = Converter.convertFeedVideosDTOsToPreviewEntity(
                motherlessRepository.getViewedVideos().take(
                    12
                )
            )
            withContext(Dispatchers.Main) {
                being_videos.setItems(
                    recentVideos
                )
                favorited_videos.setItems(
                    favoritesVideos
                )
                popular_videos.setItems(
                    mostViewedVideos
                )
            }
        }
    }
}