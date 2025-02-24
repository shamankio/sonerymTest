package com.rustan.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageResponse(
    @Json(name = "description")
    val description: String? = null,
    @Json(name = "generator")
    val generator: String? = null,
    @Json(name = "items")
    val items: List<Item?>? = null,
    @Json(name = "link")
    val link: String? = null,
    @Json(name = "modified")
    val modified: String? = null,
    @Json(name = "title")
    val title: String? = null
) {
    @JsonClass(generateAdapter = true)
    data class Item(
        @Json(name = "author")
        val author: String? = null,
        @Json(name = "author_id")
        val authorId: String? = null,
        @Json(name = "date_taken")
        val dateTaken: String? = null,
        @Json(name = "description")
        val description: String? = null,
        @Json(name = "link")
        val link: String? = null,
        @Json(name = "media")
        val media: Media? = null,
        @Json(name = "published")
        val published: String? = null,
        @Json(name = "tags")
        val tags: String? = null,
        @Json(name = "title")
        val title: String? = null
    ) {
        @JsonClass(generateAdapter = true)
        data class Media(
            @Json(name = "m")
            val m: String? = null
        )
    }
}