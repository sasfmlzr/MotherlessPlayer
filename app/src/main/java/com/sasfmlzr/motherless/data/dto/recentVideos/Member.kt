package com.sasfmlzr.motherless.data.dto.recentVideos

import com.google.gson.annotations.SerializedName

data class Member(

	@SerializedName("profile")
	val profile: String? = null,

	@SerializedName("tagline")
	val tagline: String? = null,

	@SerializedName("avatar")
	val avatar: String? = null,

	@SerializedName("username")
	val username: String? = null
)