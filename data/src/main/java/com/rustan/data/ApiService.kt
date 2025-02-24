package com.rustan.data

import com.rustan.data.model.ImageResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("photos_public.gne?format=json&nojsoncallback=1#")
    suspend fun getDataByCategory(
        @Query("tags") category: String
    ): ImageResponse
}

class ApiServiceImplement(private val apiService: ApiService) {
    suspend fun getImagesByCategory(category: String): ImageResponse {
        return apiService.getDataByCategory(category)
    }
}