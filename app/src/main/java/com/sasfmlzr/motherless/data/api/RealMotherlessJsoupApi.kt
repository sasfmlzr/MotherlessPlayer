package com.sasfmlzr.motherless.data.api

import com.sasfmlzr.motherless.entity.VideoData
import org.jsoup.Jsoup
import javax.inject.Inject

class RealMotherlessJsoupApi @Inject constructor() : MotherlessJsoupApi {
    override fun getVideoPage(url: String): VideoData {
        val doc = Jsoup.connect(url).get().body()

        val urlVideo = doc.getElementById("content").getElementsByTag("script").filter {
            it.attr("type") == "text/javascript"
        }[0].dataNodes()[0].wholeData.split("__fileurl = '")[1].split("';")[0]

        val title =
            doc.getElementById("media-info")
                .getElementsByClass("media-meta-info")
                .first()
                .getElementsByTag("h1")
                .first()
                .textNodes()
                .first()
                .text()

        val videoDetails = doc.getElementById("media-info")
            .getElementsByClass("media-meta-info")
            .first()
            .getElementsByTag("h2").map {
                val key = it.children().first().textNodes().first().text()
                val value = it.textNodes().last().text()
                Pair(key, value)
            }

        val tags = doc.getElementById("media-tags-container")
            .children()
            .map {
                it.getElementsByTag("h4")
            }
            .flatten()
            .map {
                it.children().text()
            }

        return VideoData(
            urlVideo = urlVideo,
            title = title,
            tags = tags,
            dateVideo = videoDetails[0].second,
            viewedCount = videoDetails[1].second,
            likeCount = videoDetails[2].second
        )
    }


}