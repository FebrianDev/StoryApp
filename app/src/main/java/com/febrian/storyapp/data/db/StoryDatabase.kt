package com.febrian.storyapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.febrian.storyapp.data.response.Story

@Database(
    entities = [Story::class, RemoteKeys::class],
    version = 2,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}