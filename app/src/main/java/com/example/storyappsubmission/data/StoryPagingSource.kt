package com.example.storyappsubmission.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyappsubmission.api.ApiService
import com.example.storyappsubmission.api.pojo.StoryItem
import java.lang.Exception

class StoryPagingSource(private val apiService: ApiService): PagingSource<Int, StoryItem>() {
    override fun getRefreshKey(state: PagingState<Int, StoryItem>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryItem> {
        return try{
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStoriesWithPageAndSize(position ,params.loadSize)

            val data = responseData
            Log.d("damira",data.toString())
            LoadResult.Page(
                data = data.listStory,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (data.listStory.isNullOrEmpty()) null else position + 1
            )
        }catch (exception : Exception) {
            return LoadResult.Error(exception)
        }
    }
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}