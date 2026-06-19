package io.github.alxiw.punks.data.di

import io.github.alxiw.punks.data.BeersRepositoryImpl
import io.github.alxiw.punks.domain.BeersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindBeersRepository(
        beersRepositoryImpl: BeersRepositoryImpl
    ): BeersRepository
}
