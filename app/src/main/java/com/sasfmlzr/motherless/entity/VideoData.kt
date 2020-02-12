package com.sasfmlzr.motherless.entity

data class VideoData(
    val urlVideo: String,
    val title: String,
    val tags: List<String>?,
    val dateVideo: String,
    val viewedCount: String,
    val likeCount: String
)