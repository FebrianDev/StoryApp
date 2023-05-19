package com.febrian.storyapp.ui.story

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.febrian.storyapp.R
import com.febrian.storyapp.databinding.ActivityMainBinding
import com.febrian.storyapp.ui.auth.LoginActivity
import com.febrian.storyapp.ui.maps.MapsActivity
import com.febrian.storyapp.ui.story.adapter.LoadingStateAdapter
import com.febrian.storyapp.ui.story.adapter.StoryAdapter
import com.febrian.storyapp.ui.story.callback.StoryCallback
import com.febrian.storyapp.ui.story.vm.StoryViewModel
import com.febrian.storyapp.utils.Constant
import com.febrian.storyapp.utils.Helper
import com.febrian.storyapp.utils.UserPreference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), View.OnClickListener, StoryCallback {

    private lateinit var binding: ActivityMainBinding

    private val storyViewModel: StoryViewModel by viewModels()

    @Inject
    lateinit var userPreference: UserPreference

    @Inject
    lateinit var helper: Helper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addStory.setOnClickListener(this)
        binding.maps.setOnClickListener(this)
        binding.setting.setOnClickListener(this)
        binding.logout.setOnClickListener(this)
        getData()
    }

    private fun getData() {
        val storyAdapter = StoryAdapter(this)
        binding.rvStory.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = storyAdapter.withLoadStateFooter(footer = LoadingStateAdapter {
                storyAdapter.retry()
            })
        }

        storyViewModel.stories.observe(this) {
            storyAdapter.submitData(lifecycle, it)
        }
    }

    private fun setupAction() {
        startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
    }

    private fun logout() {
        androidx.appcompat.app.AlertDialog.Builder(this).setIcon(R.drawable.baseline_logout_24)
            .setTitle(getString(R.string.logout))
            .setMessage(getString(R.string.are_you_sure_logout)).setPositiveButton(
                getString(R.string.yes)
            ) { _, _ ->
                userPreference.clearToken()
                helper.moveActivityWithFinish(this, LoginActivity())
            }.setNegativeButton(getString(R.string.no), null).show()
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.addStory -> helper.moveActivityWithFinish(this, AddNewStoryActivity())
            binding.maps -> helper.moveActivity(this, MapsActivity())
            binding.setting -> setupAction()
            binding.logout -> logout()
        }
    }

    override fun getDetail(id: String) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(Constant.ID_STORY, id)
        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
    }
}