package com.febrian.storyapp.data.response

data class DetailStoryResponse(
    var error: Boolean? = null,
    var message: String? = null,
    var story: Story? = null
)