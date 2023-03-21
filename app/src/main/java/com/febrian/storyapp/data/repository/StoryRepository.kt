package com.febrian.storyapp.data.repository

import com.febrian.storyapp.data.api.ApiService
import com.febrian.storyapp.data.response.AddStoryResponse
import com.febrian.storyapp.data.response.DetailStoryResponse
import com.febrian.storyapp.data.response.StoryResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class StoryRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun addNewStory(
        token: String, image: MultipartBody.Part, description: String
    ): Result<AddStoryResponse> {
        return try {
            val addStory = apiService.addNewStory(
                "Bearer $token", image, description.toRequestBody("text/plain".toMediaType())
            )
            Result.success(addStory)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllStories(token: String): Result<StoryResponse> {
        return try {
            Result.success(apiService.getAllStories("Bearer $token"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDetailStory(token: String, id: String): Result<DetailStoryResponse> {
        return try {
            Result.success(apiService.getDetailStory("Bearer $token", id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}