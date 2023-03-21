package com.febrian.storyapp.utils

import javax.inject.Inject

class UserPreference @Inject constructor(private val preferenceManager: PreferenceManager) {

    fun setToken(token: String?) {
        preferenceManager.putString(Constant.TOKEN, token.toString())
    }

    fun getToken(): String {
        return preferenceManager.getString(Constant.TOKEN)
    }

    fun clearToken() {
        preferenceManager.clear(Constant.TOKEN)
    }
}