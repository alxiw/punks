package com.iiinaiii.punks.di

import com.iiinaiii.punks.data.BeersRepositoryImpl
import com.iiinaiii.punks.domain.BeersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindBeersRepository(
        beersRepositoryImpl: BeersRepositoryImpl
    ): BeersRepository
}
