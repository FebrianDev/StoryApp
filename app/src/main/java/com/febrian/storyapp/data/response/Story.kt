package com.febrian.storyapp.data.response

data class Story(
    var id: String? = null,
    var name: String? = null,
    var description: String? = null,
    var photoUrl: String? = null,
    var createdAt: String? = null,
    var lat: Double? = null,
    var lon: Double? = null
)