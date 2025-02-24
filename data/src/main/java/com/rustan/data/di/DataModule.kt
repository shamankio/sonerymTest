package com.rustan.data.di

import com.rustan.data.ApiService
import com.rustan.data.ApiServiceImplement
import com.rustan.data.Repository
import com.rustan.data.RepositoryImp
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val networkModule = module {
    fun provideRetrofit(baseUrl: String, moshi: Moshi, client: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl).client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()
    }

    fun provideApi(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
    single { provideHttpClient() }
    single {
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }
    single {
        provideRetrofit(
            baseUrl = "https://api.flickr.com/services/feeds/", moshi = get(), client = get()
        )
    }
    single { provideApi(get()) }
    single { ApiServiceImplement(get()) }
    single<Repository> { RepositoryImp(get()) }
}