package com.febrian.storyapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.febrian.storyapp.data.StoryRemoteMediator
import com.febrian.storyapp.data.api.ApiService
import com.febrian.storyapp.data.db.StoryDatabase
import com.febrian.storyapp.data.response.AddStoryResponse
import com.febrian.storyapp.data.response.DetailStoryResponse
import com.febrian.storyapp.data.response.Story
import com.febrian.storyapp.data.response.StoryResponse
import com.febrian.storyapp.utils.UserPreference
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class StoryRepository @Inject constructor(
    private val database: StoryDatabase,
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    suspend fun addNewStory(
        image: MultipartBody.Part, description: RequestBody, lat: Float? = null, lon: Float? = null
    ): Result<AddStoryResponse> {
        return try {
            val token = "Bearer ${userPreference.getToken()}"
            val addStory = apiService.addNewStory(
                token,
                image,
                description,
                lat,
                lon
            )
            Result.success(addStory)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getAllStories(): Flow<PagingData<Story>> {
        val token = "Bearer ${userPreference.getToken()}"
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(token, apiService, database),
            pagingSourceFactory = {
                database.storyDao().getAllStory()
            }
        ).flow
    }

    /* fun getStories(): Flow<PagingData<Story>> {
         val token = "Bearer ${userPreference.getToken()}"
         return Pager(
             config = PagingConfig(
                 pageSize = 5
             ),
             pagingSourceFactory = {
                 StoryPagingResource(apiService, token)
             }
         ).flow
     }*/

    suspend fun getStoriesWithLocation(): Result<StoryResponse> {
        return try {
            val token = "Bearer ${userPreference.getToken()}"
            val response = apiService.getAllStories(token, size = 30, location = 1)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDetailStory(id: String): Result<DetailStoryResponse> {
        return try {
            val token = "Bearer ${userPreference.getToken()}"
            Result.success(apiService.getDetailStory(token, id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}