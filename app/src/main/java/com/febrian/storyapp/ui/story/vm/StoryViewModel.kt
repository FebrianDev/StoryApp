package com.febrian.storyapp.ui.story.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.febrian.storyapp.data.repository.StoryRepository
import com.febrian.storyapp.data.response.AddStoryResponse
import com.febrian.storyapp.data.response.DetailStoryResponse
import com.febrian.storyapp.data.response.StoryResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class StoryViewModel @Inject constructor(private val repository: StoryRepository) : ViewModel() {

    private var _resultStory = MutableLiveData<Result<StoryResponse>>()
    val resultStory: LiveData<Result<StoryResponse>> get() = _resultStory

    private var _resultDetailStory = MutableLiveData<Result<DetailStoryResponse>>()
    val resultDetailStory: LiveData<Result<DetailStoryResponse>> get() = _resultDetailStory

    private var _resultAddStory = MutableLiveData<Result<AddStoryResponse>>()
    val resultAddStory: LiveData<Result<AddStoryResponse>> get() = _resultAddStory

    private var _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> get() = _loading

    fun getAllStories(token: String) {
        viewModelScope.launch {
            _loading.value = true
            _resultStory.value = repository.getAllStories(token)
            _loading.value = false
        }
    }

    fun addNewStory(token: String, image: MultipartBody.Part, description: String) {
        viewModelScope.launch {
            _resultAddStory.value = repository.addNewStory(token, image, description)
        }
    }

    fun getDetailStory(token: String, id: String) {
        viewModelScope.launch {
            _loading.value = true
            _resultDetailStory.value = repository.getDetailStory(token, id)
            _loading.value = false
        }
    }

}