package com.test.sample_architecture.presentation.main

import com.test.sample_architecture.domain.model.Photo
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.test.sample_architecture.R
import com.test.sample_architecture.databinding.ListItemBinding
import com.test.sample_architecture.presentation.base.BaseListAdapter

class MainListAdapter: BaseListAdapter<Photo, ListItemBinding>(
    diffCallback = MainDiffCallback()) {

    override fun createBinding(parent: ViewGroup): ListItemBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item,
            parent,
            false)
    }

    override fun bind(binding: ListItemBinding, item: Photo) {
        binding.item = item
    }

    private class MainDiffCallback : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.title == newItem.title
        }
    }
}