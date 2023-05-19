package com.febrian.storyapp.ui.story

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.febrian.storyapp.R
import com.febrian.storyapp.databinding.ActivityDetailBinding
import com.febrian.storyapp.ui.story.vm.StoryViewModel
import com.febrian.storyapp.utils.Constant
import com.febrian.storyapp.utils.Helper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val storyViewModel: StoryViewModel by viewModels()

    @Inject
    lateinit var helper: Helper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra(Constant.ID_STORY)

        storyViewModel.getDetailStory(id.toString())
        observerResults()

        binding.back.setOnClickListener { finish() }
    }

    private fun observerResults() {
        storyViewModel.resultDetailStory.observe(this) {
            it.onSuccess { data ->
                helper.showToast(data.message.toString())
                binding.apply {
                    photo.loadImage(data.story?.photoUrl)
                    name.text = data.story?.name.toString()
                    description.text = data.story?.description.toString()
                }
            }
            it.onFailure { t ->
                helper.showToast(t.message.toString())
            }
        }

        storyViewModel.loading.observe(this) { active ->
            helper.showLoading(active, binding.loading)
        }
    }

    private fun ImageView.loadImage(url: String?) {
        Glide.with(this.context)
            .load(url)
            .error(R.drawable.baseline_broken_image_24)
            .into(this)
    }
}