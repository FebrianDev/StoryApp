package com.febrian.storyapp.di

import android.content.Context
import com.febrian.storyapp.utils.Helper
import com.febrian.storyapp.utils.PreferenceManager
import com.febrian.storyapp.utils.UserPreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UtilsHelper {

    //Helper
    @Provides
    @Singleton
    fun provideHelper(@ApplicationContext context: Context) = Helper(context)

    //Preference Manager
    @Provides
    @Singleton
    fun providePreferenceManager(@ApplicationContext context: Context) = PreferenceManager(context)

    //User Preference
    @Provides
    @Singleton
    fun provideUserPreference(preferenceManager: PreferenceManager): UserPreference =
        UserPreference(preferenceManager)

}