package com.febrian.storyapp.data.repository

import com.febrian.storyapp.data.api.ApiService
import com.febrian.storyapp.data.response.LoginResponse
import com.febrian.storyapp.data.response.RegisterResponse
import javax.inject.Inject

class AuthRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun register(name: String, email: String, password: String): Result<RegisterResponse> {
        return try {
            Result.success(apiService.register(name, email, password))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            Result.success(apiService.login(email, password))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}