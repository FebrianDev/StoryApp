package com.febrian.storyapp.ui.story

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.febrian.storyapp.R
import com.febrian.storyapp.databinding.ActivityAddNewStoryBinding
import com.febrian.storyapp.ui.story.vm.StoryViewModel
import com.febrian.storyapp.utils.Helper
import com.febrian.storyapp.utils.MediaUtils
import com.febrian.storyapp.utils.PickSinglePhotoContract
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import javax.inject.Inject

@AndroidEntryPoint
class AddNewStoryActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddNewStoryBinding

    private val storyViewModel: StoryViewModel by viewModels()

    @Inject
    lateinit var helper: Helper

    private var imagePart: MultipartBody.Part? = null

    private var latLng: LatLng? = null

    private val locationPermissionCode = 123
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCamera.setOnClickListener(this)
        binding.btnGallery.setOnClickListener(this)
        binding.btnUpload.setOnClickListener(this)
        binding.back.setOnClickListener(this)

        binding.switchLocation.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) return@setOnCheckedChangeListener
            if (isLocationPermissionGranted()) {
                getCurrentLocation()
            } else {
                requestLocationPermissions()
            }
        }

        observerResults()
    }

    private fun isLocationPermissionGranted(): Boolean {
        val fineLocationPermission = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        )
        val coarseLocationPermission = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
        )

        return fineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                coarseLocationPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            locationPermissionCode
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permissions granted, proceed with getting location
                getCurrentLocation()
            } else {
                helper.showToast(getString(R.string.did_get_permission))
            }
        }
    }

    private fun getCurrentLocation() {
        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            helper.showToast(getString(R.string.did_get_permission))
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                latLng = if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude

                    LatLng(latitude, longitude)

                } else {
                    LatLng(0.0, 0.0)
                }
            }.addOnFailureListener {
                helper.showToast(it.message.toString())
            }

    }

    private fun observerResults() {
        storyViewModel.resultAddStory.observe(this) {
            it.onSuccess { data ->
                if (data.error == false) {
                    helper.showToast(data.message.toString())
                    helper.moveActivityWithFinish(this, MainActivity())
                } else {
                    helper.showToast(data.message.toString())
                }
            }

            it.onFailure { t ->
                helper.showToast(t.message.toString())
            }
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            var uri = result.data?.data
            if (uri == null) {
                uri = MediaUtils.bitmapToUri(this, result.data?.extras?.get("data") as Bitmap)
            }

            uriToMultipart(uri)
        }
    }

    private fun uriToMultipart(uri: Uri?) {
        if (uri == null) return
        binding.photo.setImageURI(uri)
        val file = MediaUtils.uriToFile(uri, this)
        val newFile = MediaUtils.reduceFileImage(file)
        val requestBody: RequestBody = newFile.asRequestBody("image/*".toMediaTypeOrNull())
        imagePart =
            MultipartBody.Part.createFormData("photo", newFile.name, requestBody)
    }

    private fun pickImageFromCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        launcherIntentCamera.launch(cameraIntent)
    }

    private val singlePhotoPickerLauncher =
        registerForActivityResult(PickSinglePhotoContract()) { imageUri: Uri? ->
            uriToMultipart(imageUri)
        }

    private fun pickImageFromGallery() = singlePhotoPickerLauncher.launch()

    private fun uploadStory() {

        if (imagePart == null) {
            helper.showToast(getString(R.string.image_must_be_filled))
            return
        }

        val description = binding.description.text.toString()
        if (description.isEmpty()) {
            helper.showToast(getString(R.string.description_must_be_filled))
            return
        }

        storyViewModel.addNewStory(
            imagePart!!,
            description,
            latLng
        )
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnCamera -> pickImageFromCamera()
            binding.btnGallery -> pickImageFromGallery()
            binding.btnUpload -> uploadStory()
            binding.back -> helper.moveActivityWithFinish(AddNewStoryActivity(), MainActivity())
        }
    }
}