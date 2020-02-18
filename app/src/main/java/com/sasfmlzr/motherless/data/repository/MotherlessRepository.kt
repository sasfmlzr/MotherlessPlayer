package com.sasfmlzr.motherless.data.repository

import com.sasfmlzr.motherless.data.dto.recentVideos.FeedVideosDTO
import com.sasfmlzr.motherless.entity.VideoData

interface MotherlessRepository {
    suspend fun getRecentVideos(): List<FeedVideosDTO>
    suspend fun getLatestViewedVideos(): List<FeedVideosDTO>
    suspend fun getFavoritedVideos(): List<FeedVideosDTO>
    suspend fun getViewedVideos(): List<FeedVideosDTO>
    suspend fun getCommentedVideos(): List<FeedVideosDTO>
    suspend fun getSearchVideosByQuery(query: String): List<FeedVideosDTO>
    fun getVideoData(url: String): VideoData
}
