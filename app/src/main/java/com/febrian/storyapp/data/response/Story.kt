package com.febrian.storyapp.data.response

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "story")
data class Story(
    @PrimaryKey
    var id: String,
    var name: String? = null,
    var description: String? = null,
    var photoUrl: String? = null,
    var createdAt: String? = null,
    var lat: Double? = null,
    var lon: Double? = null
)