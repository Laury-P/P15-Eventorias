package com.openclassroom.eventorias.core.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * Ex:
     * @Binds
     *     @Singleton
     *     abstract fun bindCoopRepository(
     *         localCoopRepository: LocalCoopRepository
     *     ): CoopRepository
     */

}