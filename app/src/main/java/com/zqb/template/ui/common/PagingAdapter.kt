package com.zqb.template.ui.common

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zqb.template.resource.Resource
import com.zqb.template.resource.Status

/**
 * 分页框架-paging 通用适配器
 */
abstract class PagingAdapter<T, V : ViewDataBinding> constructor(
        diffCallback: DiffUtil.ItemCallback<T>,
        private val retryCallback: () -> Unit
) :
        PagedListAdapter<T, RecyclerView.ViewHolder>(diffCallback) {


    protected abstract fun createBinding(parent: ViewGroup): V

    private var resource: Resource<Any>? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            1 -> getItem(position)?.let { bind((holder as DataBoundViewHolder<*>).binding as V, it) }
            0 -> (holder as NetworkStateItemViewHolder).bindTo(
                    resource
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> {
                val binding = createBinding(parent)
                DataBoundViewHolder(binding)
            }
            0 -> NetworkStateItemViewHolder.create(parent, retryCallback)
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    private fun hasExtraRow() = resource?.status?:Status.RUNNING != Status.SUCCESS

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            0
        } else {
            1
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun setNetworkState(resource: Resource<Any>?) {
        val previousState = this.resource
        val hadExtraRow = hasExtraRow()
        this.resource = resource
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != resource) {
            notifyItemChanged(itemCount - 1)
        }
    }

    protected abstract fun bind(binding: V, item: T)
}
