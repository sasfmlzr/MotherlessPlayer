package com.sasfmlzr.motherless.data.api

import com.sasfmlzr.motherless.entity.VideoData

interface MotherlessJsoupApi {
    fun getVideoPage(url: String) : VideoData
}