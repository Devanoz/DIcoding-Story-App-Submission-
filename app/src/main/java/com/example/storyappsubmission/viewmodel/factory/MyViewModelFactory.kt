package com.example.storyappsubmission.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyappsubmission.viewmodel.AddStoryViewModel
import com.example.storyappsubmission.viewmodel.DetailStoryViewModel
import com.example.storyappsubmission.viewmodel.LoginViewModel
import com.example.storyappsubmission.viewmodel.RegisterViewModel

class MyViewModelFactory constructor(private val application: Application) :
    ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            LoginViewModel(application) as T
        } else if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            AddStoryViewModel(application) as T
        } else if (modelClass.isAssignableFrom(DetailStoryViewModel::class.java)) {
            DetailStoryViewModel(application) as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            RegisterViewModel(application) as T
        } else {
            throw IllegalArgumentException("Viewmodel not found")
        }
    }
}