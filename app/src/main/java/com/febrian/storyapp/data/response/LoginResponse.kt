package com.febrian.storyapp.data.response

data class LoginResponse(
    var error: Boolean? = null,
    var message: String? = null,
    var loginResult: LoginResult? = null
)