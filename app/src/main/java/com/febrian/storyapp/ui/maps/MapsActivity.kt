package com.febrian.storyapp.ui.maps

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.febrian.storyapp.R
import com.febrian.storyapp.databinding.ActivityMapsBinding
import com.febrian.storyapp.ui.story.vm.StoryViewModel
import com.febrian.storyapp.utils.Helper
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private val storyViewModel: StoryViewModel by viewModels()

    @Inject
    lateinit var helper: Helper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val mapStyleOptions = MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style)
        mMap.setMapStyle(mapStyleOptions)

        getStoryLocation()
    }

    private fun getStoryLocation() {
        storyViewModel.getStoriesWithLocation()
        storyViewModel.resultStoryWithLocation.observe(this) { result ->
            result.onSuccess { data ->

                if (data.listStory.isEmpty()) {
                    helper.showToast("Data is empty!")
                    return@onSuccess
                }

                data.listStory.forEach { story ->

                    if (story.lat != null && story.lon != null) {
                        val latLng = LatLng(story.lat!!, story.lon!!)

                        mMap.addMarker(
                            MarkerOptions().position(latLng).title(story.name)
                                .snippet("Lat: ${story.lat}, Long: ${story.lon}")
                        )
                    }
                }
            }

            result.onFailure { t ->
                helper.showToast(t.message.toString())
            }
        }
    }

}