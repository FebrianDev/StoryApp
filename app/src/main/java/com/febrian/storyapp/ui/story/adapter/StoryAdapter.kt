package com.febrian.storyapp.ui.story.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.febrian.storyapp.R
import com.febrian.storyapp.data.response.Story
import com.febrian.storyapp.databinding.ItemStoryBinding
import com.febrian.storyapp.ui.story.callback.StoryCallback

class StoryAdapter(private val callback: StoryCallback) :
    PagingDataAdapter<Story, StoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            Glide.with(itemView.context).load(story.photoUrl)
                .error(R.drawable.baseline_broken_image_24).into(binding.photo)
            binding.name.text = story.name
            binding.description.text = story.description

            itemView.setOnClickListener { callback.getDetail(story.id) }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}