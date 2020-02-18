package com.sasfmlzr.motherless.data.api

import com.sasfmlzr.motherless.data.dto.recentVideos.FeedVideosDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface MotherlessApi {

    // https://motherless.com/feeds/recent/videos?format=json  the latest videos
    @GET("/feeds/recent/videos?format=json")
    suspend fun getRecentVideos(): List<FeedVideosDTO>

    @GET("/feeds/live/videos?format=json")
    suspend fun getLatestViewedVideos(): List<FeedVideosDTO>
    //https://motherless.com/feeds/(recent|viewed|favorited|commented)/(images|videos)

    @GET("/feeds/favorited/videos?format=json")
    suspend fun getFavoritedVideos(): List<FeedVideosDTO>

    @GET("/feeds/viewed/videos?format=json")
    suspend fun getViewedVideos(): List<FeedVideosDTO>

    @GET("/feeds/commented/videos?format=json")
    suspend fun getCommentedVideos(): List<FeedVideosDTO>

    @GET("/feeds/search/{query}/videos?format=json")
    suspend fun getSearchVideosByQuery(@Path("query") query: String): List<FeedVideosDTO>
}