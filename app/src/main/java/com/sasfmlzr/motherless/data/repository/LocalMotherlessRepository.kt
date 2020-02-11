package com.sasfmlzr.motherless.data.repository

import com.sasfmlzr.motherless.data.api.MotherlessApi
import com.sasfmlzr.motherless.data.dto.recentVideos.FeedVideosDTO
import javax.inject.Inject

class LocalMotherlessRepository @Inject constructor(
    private val api: MotherlessApi
) : MotherlessRepository {

    override suspend fun getRecentVideos(): List<FeedVideosDTO> {
        return api.getRecentVideos()
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
}
