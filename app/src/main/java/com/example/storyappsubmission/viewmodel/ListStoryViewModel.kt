package com.example.storyappsubmission.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyappsubmission.api.ApiConfig
import com.example.storyappsubmission.api.ApiService
import com.example.storyappsubmission.api.pojo.StoriesResponse
import com.example.storyappsubmission.api.pojo.StoryItem
import com.example.storyappsubmission.data.StoryRepository
import com.example.storyappsubmission.data.local.PreferencesDataStoreConstans
import com.example.storyappsubmission.data.local.PreferencesDataStoreHelper
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListStoryViewModel(private val storyRepository: StoryRepository,application: Application) : AndroidViewModel(application) {
    private val token = runBlocking {
         PreferencesDataStoreHelper(application).getFirstPreference(
            PreferencesDataStoreConstans.TOKEN,
            ""
        )
    }
    private lateinit var client: ApiService

    private val _storyList = MutableLiveData<List<StoryItem>>()
    val storyList: LiveData<List<StoryItem>> = _storyList

    var stories: LiveData<PagingData<StoryItem>> =
        storyRepository.getStories().cachedIn(viewModelScope)

    init {
        client = ApiConfig.getApiServiceWithToken(token)
//        getAllStories()
    }

    fun refreshStoryList() {
        stories = storyRepository.getStories().cachedIn(viewModelScope)
    }

//    fun getAllStories() {
//
//        val call = client.getAllStories()
//        call.enqueue(object : Callback<StoriesResponse> {
//            override fun onResponse(
//                call: Call<StoriesResponse>,
//                response: Response<StoriesResponse>
//            ) {
//                if (response.isSuccessful) {
//                    val storyList = response.body()?.listStory
//                    _storyList.value = storyList
//                }
//            }
//
//            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
//                //show snackbar
//            }
//        })
//    }
}