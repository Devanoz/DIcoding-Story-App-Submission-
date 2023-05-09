package com.example.storyappsubmission.di

import android.content.Context
import com.example.storyappsubmission.api.ApiConfig
import com.example.storyappsubmission.data.StoryRepository
import com.example.storyappsubmission.data.local.PreferencesDataStoreConstans
import com.example.storyappsubmission.data.local.PreferencesDataStoreHelper
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val token = runBlocking {
            PreferencesDataStoreHelper(context).getFirstPreference(
                PreferencesDataStoreConstans.TOKEN,
                ""
            )
        }
        val apiService = ApiConfig.getApiServiceWithToken(token)
        return StoryRepository(apiService)
    }
}