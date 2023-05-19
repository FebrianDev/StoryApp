package com.febrian.storyapp.ui.story.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.febrian.storyapp.data.repository.StoryRepository
import com.febrian.storyapp.data.response.AddStoryResponse
import com.febrian.storyapp.data.response.DetailStoryResponse
import com.febrian.storyapp.data.response.Story
import com.febrian.storyapp.data.response.StoryResponse
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class StoryViewModel @Inject constructor(private val repository: StoryRepository) : ViewModel() {

    private var _resultStoryWithLocation = MutableLiveData<Result<StoryResponse>>()
    val resultStoryWithLocation: LiveData<Result<StoryResponse>> get() = _resultStoryWithLocation

    private var _resultDetailStory = MutableLiveData<Result<DetailStoryResponse>>()
    val resultDetailStory: LiveData<Result<DetailStoryResponse>> get() = _resultDetailStory

    private var _resultAddStory = MutableLiveData<Result<AddStoryResponse>>()
    val resultAddStory: LiveData<Result<AddStoryResponse>> get() = _resultAddStory

    val stories: LiveData<PagingData<Story>>
        get() = repository.getAllStories().cachedIn(viewModelScope).asLiveData()

    private var _loading = MutableLiveData(true)
    val loading: LiveData<Boolean> get() = _loading

    fun addNewStory(image: MultipartBody.Part, description: String, latLng: LatLng? = null) {
        viewModelScope.launch {
            _resultAddStory.value = repository.addNewStory(
                image,
                description.toRequestBody("text/plain".toMediaType()),
                latLng?.latitude?.toFloat(),
                latLng?.longitude?.toFloat()
            )
        }
    }

    fun getStoriesWithLocation() {
        viewModelScope.launch {
            _loading.value = true
            _resultStoryWithLocation.value = repository.getStoriesWithLocation()
            _loading.value = false
        }
    }

    fun getDetailStory(id: String) {
        viewModelScope.launch {
            _loading.value = true
            _resultDetailStory.value = repository.getDetailStory(id)
            _loading.value = false
        }
    }

}