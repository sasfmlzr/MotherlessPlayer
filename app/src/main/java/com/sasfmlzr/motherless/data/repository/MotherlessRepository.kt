package com.sasfmlzr.motherless.data.repository

import com.sasfmlzr.motherless.data.dto.recentVideos.FeedVideosDTO

interface MotherlessRepository {
    suspend fun getRecentVideos(): List<FeedVideosDTO>
    suspend fun getFavoritedVideos(): List<FeedVideosDTO>
    suspend fun getViewedVideos(): List<FeedVideosDTO>
    suspend fun getCommentedVideos(): List<FeedVideosDTO>
}
