package com.iiinaiii.punks.data.di

import android.content.Context
import android.util.Log
import com.iiinaiii.punks.data.remote.api.BeersSearchService
import com.iiinaiii.punks.data.remote.api.util.NetworkHandler
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Dagger module providing classes required to beers with data.
 */
@Module
@InstallIn(SingletonComponent::class)
class BeersDataModule {

    companion object {
        private const val CACHE_SIZE = 10 * 1024 * 1024L // 10 MB
        private const val CACHE_MAX_STALE = 60 * 60 * 24 * 7 // 7 days
        private const val CACHE_MAX_AGE = 60 * 3 // 3 minutes
    }

    @Provides
    @Singleton
    internal fun provideNetworkHandler(@ApplicationContext context: Context): NetworkHandler {
        return NetworkHandler(context)
    }

    @Provides
    @Singleton
    internal fun provideCache(@ApplicationContext context: Context): Cache {
        return Cache(File(context.cacheDir, "http_cache"), CACHE_SIZE)
    }

    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    fun provideOkHttpClient(cache: Cache, handler: NetworkHandler): OkHttpClient {
        val interceptor = HttpLoggingInterceptor { message -> Log.d("HELLO", message) }
            .apply { level = HttpLoggingInterceptor.Level.BASIC }
        return OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .cache(cache)
            .addInterceptor { chain ->
                var request = chain.request()
                if (!handler.isConnected) {
                    request = request.newBuilder()
                        .header(
                            "Cache-Control",
                            "public, only-if-cached, max-stale=$CACHE_MAX_STALE"
                        )
                        .build()
                }
                chain.proceed(request)
            }
            .addNetworkInterceptor { chain ->
                val response = chain.proceed(chain.request())
                response.newBuilder()
                    .header(
                        "Cache-Control",
                        "public, max-age=$CACHE_MAX_AGE"
                    )
                    .removeHeader("Pragma")
                    .build()
            }
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
