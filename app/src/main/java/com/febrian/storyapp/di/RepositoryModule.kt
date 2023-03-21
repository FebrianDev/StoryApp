package com.febrian.storyapp.di

import com.febrian.storyapp.data.api.ApiService
import com.febrian.storyapp.data.repository.AuthRepository
import com.febrian.storyapp.data.repository.StoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    //Provide Auth Repository
    @Provides
    @Singleton
    fun provideAuthRepository(apiService: ApiService) = AuthRepository(apiService)

    //Provide Story Repository
    @Provides
    @Singleton
    fun provideStoryRepository(apiService: ApiService) = StoryRepository(apiService)
}