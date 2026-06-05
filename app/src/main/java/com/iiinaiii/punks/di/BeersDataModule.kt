package com.iiinaiii.punks.di

import android.util.Log
import com.iiinaiii.punks.data.remote.api.BeersSearchService
import com.iiinaiii.punks.util.moshi.LocalDateTimeAdapter
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Dagger module providing classes required to beers with data.
 */
@Module
@InstallIn(SingletonComponent::class)
class BeersDataModule {

    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(LocalDateTimeAdapter)
            .build()
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor { message -> Log.d("HELLO", message) }
            .apply { level = HttpLoggingInterceptor.Level.BASIC }
        return OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    fun provideBeersSearchService(moshi: Moshi, client: OkHttpClient): BeersSearchService {
        return Retrofit.Builder()
            .baseUrl(BeersSearchService.ENDPOINT)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            .create(BeersSearchService::class.java)
    }
}