package com.febrian.storyapp.ui.story.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.febrian.storyapp.R
import com.febrian.storyapp.data.response.Story
import com.febrian.storyapp.databinding.ItemStoryBinding
import com.febrian.storyapp.ui.story.callback.StoryCallback

class StoryAdapter(private var listStory: ArrayList<Story>, private val callback: StoryCallback) :
    RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    fun setItem(listStory: ArrayList<Story>) {
        this.listStory = listStory
    }

    inner class ViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            Glide.with(itemView.context).load(story.photoUrl)
                .error(R.drawable.baseline_broken_image_24).into(binding.photo)
            binding.name.text = story.name
            binding.description.text = story.description

            itemView.setOnClickListener { callback.getDetail(story.id.toString()) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listStory[position])
    }

    override fun getItemCount(): Int = listStory.size
}