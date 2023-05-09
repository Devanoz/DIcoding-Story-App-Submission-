package com.example.storyappsubmission.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyappsubmission.data.StoryRepository

class ListStoryViewModelFactory constructor(private val application: Application,private val storyRepository: StoryRepository) :
    ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ListStoryViewModel::class.java)) {
            ListStoryViewModel(storyRepository,application) as T
        } else {
            throw IllegalArgumentException("Viewmodel not found")
        }
    }
}