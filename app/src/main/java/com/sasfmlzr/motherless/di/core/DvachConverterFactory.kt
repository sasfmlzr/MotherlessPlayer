package com.sasfmlzr.motherless.di.core

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.sasfmlzr.motherless.data.dto.recentVideos.FeedVideosDTO
import retrofit2.converter.gson.GsonConverterFactory

internal fun getOwnerContactConverterFactory(): GsonConverterFactory {
    val ownerContactsParser = GsonBuilder()
        .registerTypeAdapter(FeedVideosDTO::class.java,
            JsonDeserializer<FeedVideosDTO> { json, _, _ ->
                val jComment = json.asJsonObject
                GsonBuilder().create().fromJson(jComment, FeedVideosDTO::class.java)
            })
        .create()
    return GsonConverterFactory.create(ownerContactsParser)
}
