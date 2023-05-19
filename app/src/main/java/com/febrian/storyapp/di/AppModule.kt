package com.febrian.storyapp.di

import android.app.Application
import androidx.room.Room
import com.febrian.storyapp.BuildConfig
import com.febrian.storyapp.data.api.ApiService
import com.febrian.storyapp.data.db.RemoteKeysDao
import com.febrian.storyapp.data.db.StoryDao
import com.febrian.storyapp.data.db.StoryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    //Provide Retrofit
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {

        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder().baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(GsonConverterFactory.create()).client(client).build()
    }

    //Provide Api Service
    @Provides
    @Singleton
    fun provideAPi(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(application: Application): StoryDatabase {
        return Room.databaseBuilder(application, StoryDatabase::class.java, "story_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideStoryDao(database: StoryDatabase): StoryDao {
        return database.storyDao()
    }

    @Provides
    @Singleton
    fun provideRemoteKeysDao(database: StoryDatabase): RemoteKeysDao {
        return database.remoteKeysDao()
    }

}