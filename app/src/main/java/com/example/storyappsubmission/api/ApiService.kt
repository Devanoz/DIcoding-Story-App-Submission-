package com.example.storyappsubmission.api

import com.example.storyappsubmission.api.pojo.AddStoryResponse
import com.example.storyappsubmission.api.pojo.DetailStoryResponse
import com.example.storyappsubmission.api.pojo.LoginResponse
import com.example.storyappsubmission.api.pojo.RegisterResponse
import com.example.storyappsubmission.api.pojo.StoriesResponse
import com.example.storyappsubmission.data.model.LoginModel
import com.example.storyappsubmission.data.model.RegisterModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("register")
    fun registerUser(@Body registerModel: RegisterModel): Call<RegisterResponse>

    @POST("login")
    fun login(@Body loginModel: LoginModel): Call<LoginResponse>

    @GET("stories")
    fun getAllStories(): Call<StoriesResponse>

    @GET("stories")
    suspend fun getStoriesWithPageAndSize(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") isEnabled: Int = 1
    ): StoriesResponse

    @GET("stories?location=1")
    suspend fun getStoriesWithLocation(
    ): StoriesResponse

    @Multipart
    @POST("stories")
    fun postStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Call<AddStoryResponse>

    @GET("stories/{id}")
    fun getStoryDetail(@Path("id") id: String): Call<DetailStoryResponse>
}