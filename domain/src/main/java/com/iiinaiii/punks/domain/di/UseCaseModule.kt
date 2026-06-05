package com.iiinaiii.punks.domain.di

import com.iiinaiii.punks.domain.GetBeerUseCase
import com.iiinaiii.punks.domain.GetBeerUseCaseImpl
import com.iiinaiii.punks.domain.SearchBeersUseCase
import com.iiinaiii.punks.domain.SearchBeersUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    @Singleton
    abstract fun bindSearchBeersUseCase(
        searchBeersUseCaseImpl: SearchBeersUseCaseImpl
    ): SearchBeersUseCase

    @Binds
    @Singleton
    abstract fun bindGetBeerUseCase(
        getBeerUseCaseImpl: GetBeerUseCaseImpl
    ): GetBeerUseCase
}
