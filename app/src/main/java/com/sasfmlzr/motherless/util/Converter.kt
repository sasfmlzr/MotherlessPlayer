package com.sasfmlzr.motherless.util

import com.sasfmlzr.motherless.data.dto.recentVideos.FeedVideosDTO
import com.sasfmlzr.motherless.entity.PreviewData
import org.joda.time.Instant
import org.joda.time.LocalTime

class Converter {


    //https://cdn5-videos.motherlessmedia.com/videos/59ED6CE.mp4
    //https://cdn5-videos.motherlessmedia.com/videos/23A019F.jpg?from_helper

    //https://cdn5-thumbs.motherlessmedia.com/thumbs/744E13D-small.jpg?from_helper
    //https://cdn5-thumbs.motherlessmedia.com/thumbs/744E13D-strip.jpg?from_helper
    //data-strip-src="https://cdn5-thumbs.motherlessmedia.com/thumbs/744E13D-strip.jpg?from_helper"


    /*

    <img class="strip" src="https://cdn5-thumbs.motherlessmedia.com/thumbs/744E13D-strip.jpg?from_helper"
    alt="Videos :)" style="height: 459px; width: 2500px; clip: rect(205px, 1200px, 205160px, 1000px); left: -1000px; display: none;">

     */

    companion object {


        fun convertThumbLinkToVideosLink(link: String): String {
            return link.replace("thumbs", "videos").replace(".jpg", ".mp4")
        }

        fun convertFeedVideosDTOsToPreviewEntity(feedVideosDTOs: List<FeedVideosDTO>): List<PreviewData> {
            return feedVideosDTOs.map {
                PreviewData(
                    title = it.title ?: "",
                    link = it.link ?: "",
                    sizeSeconds = LocalTime(0, 0, 0).plusSeconds(it.size?.seconds?.toInt() ?: 0),
                    thumbnailLink = it.thumbnail ?: "",
                    tags = it.tags?.requireNoNulls() ?: listOf(),
                    time = Instant.ofEpochSecond(it.time?.toLong() ?: 0)
                )
            }
        }
    }
}