package com.example.storyappsubmission.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyappsubmission.api.ApiService
import com.example.storyappsubmission.api.pojo.StoriesResponse
import com.example.storyappsubmission.api.pojo.StoryItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryRepository(private val apiService: ApiService) {
    fun getStories(): LiveData<PagingData<StoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
    }

    suspend fun getStoriesWithLocation(): List<StoryItem> {
        return apiService.getStoriesWithLocation().listStory
    }
}