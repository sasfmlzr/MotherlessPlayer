package com.sasfmlzr.motherless.data.dto.recentVideos

import com.google.gson.annotations.SerializedName

data class Size(

	@SerializedName("seconds")
	val seconds: String? = null,

	@SerializedName("width")
	val width: String? = null,

	@SerializedName("height")
	val height: String? = null
)