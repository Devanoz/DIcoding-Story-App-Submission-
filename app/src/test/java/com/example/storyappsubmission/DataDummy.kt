package com.example.storyappsubmission

import com.example.storyappsubmission.api.pojo.StoryItem

object DataDummy {
    fun generateDummyStories(): List<StoryItem> {
        val storyList = ArrayList<StoryItem>()
        for (i in 0..5) {
            val story = StoryItem(
                "https://story-api.dicoding.dev/images/stories/photos-1683721395307_hdWkLCfd.jpg",
                "2023-05-10T12:23:15.309Z",
                "example name",
                "example description",
                0.02,
                "id $i",
                0.03
            )
            storyList.add(story)
        }
        return storyList
    }
}