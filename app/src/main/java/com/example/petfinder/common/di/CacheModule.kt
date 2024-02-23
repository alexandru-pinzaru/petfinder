package com.example.petfinder.common.di

import com.example.petfinder.common.data.cache.CacheManager
import com.example.petfinder.common.data.cache.StoroCacheManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CacheModule {

    @Provides
    @Singleton
    fun provideCacheManager(): CacheManager {
        return StoroCacheManager()
    }

}