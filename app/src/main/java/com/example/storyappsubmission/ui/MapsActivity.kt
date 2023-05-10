package com.example.storyappsubmission.ui

import android.content.ContentValues.TAG
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.storyappsubmission.R
import com.example.storyappsubmission.databinding.FragmentMapsBinding
import com.example.storyappsubmission.viewmodel.ListStoryViewModel
import com.example.storyappsubmission.viewmodel.MapViewModel
import com.example.storyappsubmission.viewmodel.factory.ListStoryViewModelFactory
import com.example.storyappsubmission.viewmodel.factory.MapViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: FragmentMapsBinding

    private lateinit var mMap: GoogleMap
    private lateinit var viewModel: MapViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel =  ViewModelProvider(this, MapViewModelFactory(application))[MapViewModel::class.java]
        lifecycleScope.launch {
            viewModel.getStoriesWithLocation()
        }
    }


    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "failed to parse")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }



    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val boundsBuilder = LatLngBounds.builder()
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        setMapStyle()
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        viewModel.stories.observe(this){storyList ->
            storyList?.forEach {
                mMap.addMarker(
                    MarkerOptions()
                        .position( LatLng(it.lat,it.lon))
                        .title(it.name)
                )?.showInfoWindow()
                boundsBuilder.include( LatLng(it.lat,it.lon))
            }.also {
                val bounds: LatLngBounds = boundsBuilder.build()
                mMap.animateCamera(
                    CameraUpdateFactory.newLatLngBounds(
                        bounds,
                        resources.displayMetrics.widthPixels,
                        resources.displayMetrics.heightPixels,
                        300
                    )
                )
            }
        }


    }
}