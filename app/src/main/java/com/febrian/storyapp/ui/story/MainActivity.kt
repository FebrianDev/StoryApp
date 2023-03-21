package com.febrian.storyapp.ui.story

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.febrian.storyapp.R
import com.febrian.storyapp.data.response.Story
import com.febrian.storyapp.databinding.ActivityMainBinding
import com.febrian.storyapp.ui.auth.LoginActivity
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

    private lateinit var storyAdapter: StoryAdapter

    private val storyViewModel: StoryViewModel by viewModels()

    @Inject
    lateinit var userPreference: UserPreference

    @Inject
    lateinit var helper: Helper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        storyAdapter = StoryAdapter(arrayListOf(), this)
        storyViewModel.getAllStories(userPreference.getToken())

        binding.addStory.setOnClickListener(this)
        binding.logout.setOnClickListener(this)

        observerResults()
    }

    private fun observerResults() {
        storyViewModel.resultStory.observe(this) {
            it.onSuccess { data ->
                helper.showToast(data.message.toString())
                showData(data.listStory as ArrayList<Story>)
            }
            it.onFailure { t ->
                helper.showToast(t.message.toString())
            }
        }

        storyViewModel.loading.observe(this) { active ->
            helper.showLoading(active, binding.loading)
        }
    }

    private fun showData(listStory: ArrayList<Story>) {
        storyAdapter.setItem(listStory)
        binding.rvStory.setHasFixedSize(true)
        binding.rvStory.layoutManager = LinearLayoutManager(this)
        binding.rvStory.adapter = storyAdapter
    }

    private fun logout() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setIcon(R.drawable.baseline_logout_24)
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
            binding.addStory -> helper.moveActivity(this, AddNewStoryActivity())
            binding.logout -> logout()
        }
    }

    override fun getDetail(id: String) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(Constant.ID_STORY, id)
        startActivity(intent)
    }
}