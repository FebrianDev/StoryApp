package com.febrian.storyapp.ui

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.febrian.storyapp.databinding.ActivitySplashScreenBinding
import com.febrian.storyapp.ui.auth.RegisterActivity
import com.febrian.storyapp.ui.story.MainActivity
import com.febrian.storyapp.utils.Helper
import com.febrian.storyapp.utils.UserPreference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    @Inject
    lateinit var userPreference: UserPreference

    @Inject
    lateinit var helper: Helper

    companion object {
        const val DELAY = 1500L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            if (userPreference.getToken().isEmpty()) {
                helper.moveActivityWithFinish(this, RegisterActivity())
            } else {
                helper.moveActivityWithFinish(this, MainActivity())
            }
        }, DELAY)

        logoAnimation()
        textAnimation()

    }

    private fun logoAnimation() {
        ObjectAnimator.ofFloat(binding.logo, View.TRANSLATION_Y, 0f, 320f).apply {
            duration = 1500
        }.start()

        ObjectAnimator.ofFloat(binding.logo, View.ALPHA, 0f, 1f).apply {
            duration = 1500
        }.start()

        ObjectAnimator.ofFloat(binding.logo, View.SCALE_X, 0f, 1f).apply {
            duration = 1500
        }.start()

        ObjectAnimator.ofFloat(binding.logo, View.SCALE_Y, 0f, 1f).apply {
            duration = 1500
        }.start()
    }

    private fun textAnimation() {
        ObjectAnimator.ofFloat(binding.text, View.TRANSLATION_Y, 0f, -320f).apply {
            duration = 1500
        }.start()

        ObjectAnimator.ofFloat(binding.text, View.ALPHA, 0f, 1f).apply {
            duration = 1500
        }.start()

        ObjectAnimator.ofFloat(binding.text, View.SCALE_X, 0f, 1f).apply {
            duration = 1500
        }.start()

        ObjectAnimator.ofFloat(binding.text, View.SCALE_Y, 0f, 1f).apply {
            duration = 1500
        }.start()
    }
}