package com.sasfmlzr.motherless.data.repository

import com.sasfmlzr.motherless.data.api.MotherlessApi
import com.sasfmlzr.motherless.data.api.MotherlessJsoupApi
import com.sasfmlzr.motherless.data.dto.recentVideos.FeedVideosDTO
import com.sasfmlzr.motherless.entity.VideoData
import javax.inject.Inject

class LocalMotherlessRepository @Inject constructor(
    private val api: MotherlessApi,
    private val jsoupApi: MotherlessJsoupApi
) : MotherlessRepository {

    override suspend fun getRecentVideos(): List<FeedVideosDTO> {
        return api.getRecentVideos()
    }

    override suspend fun getLatestViewedVideos(): List<FeedVideosDTO> {
        return api.getLatestViewedVideos()
    }

    override suspend fun getFavoritedVideos(): List<FeedVideosDTO> {
        return api.getFavoritedVideos()
    }

    override suspend fun getViewedVideos(): List<FeedVideosDTO> {
        return api.getViewedVideos()
    }

    override suspend fun getCommentedVideos(): List<FeedVideosDTO> {
        return api.getCommentedVideos()
    }

    override fun getVideoData(url: String): VideoData {
        return jsoupApi.getVideoPage(url)
    }
}
