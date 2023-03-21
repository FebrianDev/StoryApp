package com.febrian.storyapp.data.api

import com.febrian.storyapp.data.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    //register
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse


    //Login
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String, @Field("password") password: String
    ): LoginResponse


    // Add Stories
    @Multipart
    @POST("stories")
    suspend fun addNewStory(
        @Header("Authorization") token: String,
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): AddStoryResponse


    //Get All Stories
    @GET("stories")
    suspend fun getAllStories(@Header("Authorization") token: String): StoryResponse


    //Get Detail Stories
    @GET("stories/{id}")
    suspend fun getDetailStory(
        @Header("Authorization") token: String, @Path("id") id: String
    ): DetailStoryResponse
}