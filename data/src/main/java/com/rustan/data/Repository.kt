package com.rustan.data

import com.rustan.model.ImageItem

interface Repository {
    suspend fun getImage(category: String): List<ImageItem>
}

class RepositoryImp(private val apiService: ApiServiceImplement) : Repository {
    override suspend fun getImage(category: String): List<ImageItem> {
        return apiService.getImagesByCategory(category).items?.map {
            ImageItem(
                author = it?.author,
                authorId = it?.authorId,
                dateTaken = it?.dateTaken,
                description = it?.description,
                link = it?.link,
                urlString = it?.media?.m,
                published = it?.published,
                tags = it?.tags,
                title = it?.title
            )
        } ?: emptyList()
    }
}

