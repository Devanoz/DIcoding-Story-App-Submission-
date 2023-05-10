package com.example.storyappsubmission.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyappsubmission.api.pojo.StoryItem
import com.example.storyappsubmission.data.StoryRepository

class MapViewModel(private val storyRepository: StoryRepository): ViewModel() {

    private val _stories = MutableLiveData<List<StoryItem>>()
    val stories: LiveData<List<StoryItem>> = _stories

    suspend fun getStoriesWithLocation() {
        _stories.value = storyRepository.getStoriesWithLocation()
    }
}