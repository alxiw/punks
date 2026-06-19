package io.github.alxiw.punks.data.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import io.github.alxiw.punks.data.local.db.PunksDatabase
import io.github.alxiw.punks.data.local.dao.FavoriteDao
import io.github.alxiw.punks.data.remote.api.BeersSearchService
import io.github.alxiw.punks.data.remote.NetworkHandler
import io.github.alxiw.punks.data.remote.util.CacheUtil
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

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    private const val CACHE_MAX_STALE = 60 * 60 * 24 * 14 // 14 days
    private const val CACHE_MAX_AGE = 60 * 60 * 24 * 1 // 1 day
    private const val DB_NAME = "punks.db"

    @Provides
    @Singleton
    internal fun provideNetworkHandler(@ApplicationContext context: Context): NetworkHandler {
        return NetworkHandler(context)
    }

    @Provides
    @Singleton
    internal fun provideCache(@ApplicationContext context: Context): Cache {
        val cacheDir: File = CacheUtil.createDefaultCacheDir(context)
        val maxSize = CacheUtil.calculateDiskCacheSize(cacheDir)

        return Cache(cacheDir, maxSize)
    }

    @Provides
    @Singleton
    internal fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
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
                val cacheControl = if (response.isSuccessful) {
                    "public, max-age=$CACHE_MAX_AGE"
                } else {
                    "no-store"
                }
                response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .removeHeader("Expires")
                    .header("Cache-Control", cacheControl)
                    .build()
            }
            .build()
    }

    @Provides
    @Singleton
    internal fun provideBeersSearchService(moshi: Moshi, client: OkHttpClient): BeersSearchService {
        return Retrofit.Builder()
            .baseUrl(BeersSearchService.ENDPOINT)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(BeersSearchService::class.java)
    }

    @Provides
    @Singleton
    internal fun provideDatabase(@ApplicationContext context: Context): PunksDatabase {
        return Room.databaseBuilder(
            context,
            PunksDatabase::class.java,
            DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    internal fun provideFavoriteBeerDao(database: PunksDatabase): FavoriteDao {
        return database.favoriteBeerDao()
    }
}
