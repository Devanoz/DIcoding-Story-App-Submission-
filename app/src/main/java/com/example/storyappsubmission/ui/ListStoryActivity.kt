package com.example.storyappsubmission.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.storyappsubmission.R
import com.example.storyappsubmission.adapter.LoadingStateAdapter
import com.example.storyappsubmission.adapter.StoriesAdapter
import com.example.storyappsubmission.data.local.PreferencesDataStoreConstans
import com.example.storyappsubmission.data.local.PreferencesDataStoreHelper
import com.example.storyappsubmission.databinding.ActivityListStoryBinding
import com.example.storyappsubmission.viewmodel.ListStoryViewModel
import com.example.storyappsubmission.viewmodel.factory.ListStoryViewModelFactory
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListStoryBinding
    private lateinit var preferencesHelper: PreferencesDataStoreHelper
    private lateinit var storiesAdapter: StoriesAdapter
    private lateinit var toolbarStory: MaterialToolbar
    private lateinit var fabAddStory: ExtendedFloatingActionButton
    private lateinit var srlRefreshStory: SwipeRefreshLayout

    private lateinit var adapter: StoriesAdapter

    private lateinit var listStoryViewModel: ListStoryViewModel

    private val launchAddStoryForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == REQUEST_UPLOAD_SUCCES_CONDITION) {
                adapter.refresh()
                getData()
            }
        }

    private lateinit var name: String
    private lateinit var userId: String
    private lateinit var token: String
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        splashScreen.setKeepOnScreenCondition { true }
        preferencesHelper = PreferencesDataStoreHelper(this.applicationContext)
        srlRefreshStory = binding.srlRefreshStory

        toolbarStory = binding.toolbarStory
        setSupportActionBar(toolbarStory)
        fabAddStory = binding.fabAddStory

        listStoryViewModel =
            ViewModelProvider(this, ListStoryViewModelFactory(application))[ListStoryViewModel::class.java]

        lifecycleScope.launch {
            getUserName()
            if (name.isEmpty()) {
                startActivity(Intent(this@ListStoryActivity, LoginActivity::class.java))
                finish()
                splashScreen.setKeepOnScreenCondition { false }
            } else {
                splashScreen.setKeepOnScreenCondition { false }
            }
        }
        toolbarStory.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.logout -> {
                    lifecycleScope.launch {
                        logout()
                        startActivity(Intent(this@ListStoryActivity, LoginActivity::class.java))
                        finish()
                    }
                    true
                }
                R.id.map_menu -> {
                    startActivity(Intent(this,MapsActivity::class.java))
                    true
                }

                else -> false
            }
        }
        getData()
        srlRefreshStory.setOnRefreshListener {
            adapter.refresh()
            getData()
            srlRefreshStory.isRefreshing = false
        }
        fabAddStory.setOnClickListener {
            launchAddStoryForResult.launch(Intent(this, AddStoryActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.story_menu, menu)
        return true
    }

    private fun getData() {
        adapter = StoriesAdapter()
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        binding.rvStory.layoutManager = LinearLayoutManager(this)
        listStoryViewModel.stories.observe(this) {
            adapter.submitData(lifecycle, it)
            Log.d("faah",it.toString())
        }
    }

    private suspend fun getUserName() {
        withContext(Dispatchers.IO) {
            name = preferencesHelper.getFirstPreference(PreferencesDataStoreConstans.NAME, "")
            userId = preferencesHelper.getFirstPreference(PreferencesDataStoreConstans.USER_ID, "")
            token = preferencesHelper.getFirstPreference(PreferencesDataStoreConstans.TOKEN, "")
        }
    }


    private suspend fun logout() {
        withContext(Dispatchers.IO) {
            preferencesHelper.clearAllPreference()
        }
    }

    companion object {
        const val REQUEST_UPLOAD_SUCCES_CONDITION = 12340
    }

}