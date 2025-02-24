package com.rustan.model

import kotlinx.serialization.Serializable

@Serializable
data class ImageItem(
    val author: String? = null,
    val authorId: String? = null,
    val dateTaken: String? = null,
    val description: String? = null,
    val link: String? = null,
    val urlString: String? = null,
    val published: String? = null,
    val tags: String? = null,
    val title: String? = null
)