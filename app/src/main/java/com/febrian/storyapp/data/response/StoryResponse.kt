package com.febrian.storyapp.data.response

data class StoryResponse(
    var error: Boolean? = null,
    var message: String? = null,
    var listStory: ArrayList<Story> = ArrayList()
)