package com.iiinaiii.punks.di

import com.iiinaiii.punks.data.CoroutinesDispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers

/**
 * Provide [CoroutinesDispatcherProvider] to this app's components.
 */
@Module
@InstallIn(SingletonComponent::class)
class CoroutinesDispatcherProviderModule {
    @Provides
    fun provideCoroutinesDispatcherProvider() = CoroutinesDispatcherProvider(
        Dispatchers.Main, Dispatchers.Default, Dispatchers.IO
    )
}
