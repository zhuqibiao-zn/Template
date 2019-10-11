package com.zqb.template.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.zqb.template.databinding.ItemTextBinding
import com.zqb.template.ui.common.PagingAdapter

class TextPagingAdapter(private val retryCallback: () -> Unit) :
    PagingAdapter<String, ItemTextBinding>(object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return true
        }
    }, retryCallback) {
    override fun createBinding(parent: ViewGroup): ItemTextBinding {
        return ItemTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(binding: ItemTextBinding, item: String) {
        binding.data = item
    }
}