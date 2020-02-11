package com.sasfmlzr.motherless.data.dto.recentVideos

import com.google.gson.annotations.SerializedName

data class FeedVideosDTO(

	@SerializedName("thumbnail")
	val thumbnail: String? = null,

	@SerializedName("size")
	val size: Size? = null,

	@SerializedName("bytes")
	val bytes: String? = null,

	@SerializedName("codename")
	val codename: String? = null,

	@SerializedName("member")
	val member: Member? = null,

	@SerializedName("link")
	val link: String? = null,

	@SerializedName("time")
	val time: String? = null,

	@SerializedName("title")
	val title: String? = null,

	@SerializedName("mediatype")
	val mediatype: String? = null,

	@SerializedName("tags")
	val tags: List<String?>? = null
)