package com.sasfmlzr.motherless.entity

import org.joda.time.Instant
import org.joda.time.LocalTime

sealed class UnknownPreviewData

data class PreviewData(
    val title: String,
    val link: String,
    val thumbnailLink: String,
    val time: Instant,
    val tags: List<String>,
    val sizeSeconds: LocalTime
) : UnknownPreviewData()

class NullPreviewData : UnknownPreviewData()