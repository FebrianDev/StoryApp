package com.febrian.storyapp.ui.story

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.febrian.storyapp.R
import com.febrian.storyapp.databinding.ActivityAddNewStoryBinding
import com.febrian.storyapp.ui.story.vm.StoryViewModel
import com.febrian.storyapp.utils.Helper
import com.febrian.storyapp.utils.MediaUtils
import com.febrian.storyapp.utils.UserPreference
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import javax.inject.Inject

@AndroidEntryPoint
class AddNewStoryActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddNewStoryBinding

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
        )
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    private val storyViewModel: StoryViewModel by viewModels()

    @Inject
    lateinit var helper: Helper

    @Inject
    lateinit var userPreference: UserPreference

    private var imagePart: MultipartBody.Part? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.btnCamera.setOnClickListener(this)
        binding.btnGallery.setOnClickListener(this)
        binding.btnUpload.setOnClickListener(this)
        binding.back.setOnClickListener(this)

        observerResults()
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

            binding.photo.setImageURI(uri)

            val file = MediaUtils.uriToFile(uri!!, this)
            val newFile = MediaUtils.reduceFileImage(file)
            val requestBody: RequestBody = newFile.asRequestBody("image/*".toMediaTypeOrNull())
            imagePart =
                MultipartBody.Part.createFormData("photo", newFile.name, requestBody)

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                helper.showToast(getString(R.string.did_not_get_permission))
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun pickImageFromCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        launcherIntentCamera.launch(cameraIntent)
    }

    private fun pickImageFromGallery() {
        val galleryIntent = Intent()
        galleryIntent.type = "image/*"
        galleryIntent.action = Intent.ACTION_GET_CONTENT
        galleryIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        launcherIntentCamera.launch(galleryIntent)
    }

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
            userPreference.getToken(),
            imagePart!!,
            description
        )
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnCamera -> pickImageFromCamera()
            binding.btnGallery -> pickImageFromGallery()
            binding.btnUpload -> uploadStory()
            binding.back -> finish()
        }
    }
}