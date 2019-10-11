package com.zqb.template.util

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import java.lang.ref.WeakReference

fun Adapter<*>.bindEmptyView(emptyView: View, lifecycle: Lifecycle) {
    lifecycle.addObserver(EmptyLifecycleObserver(emptyView, this))
}

class EmptyLifecycleObserver(emptyView: View, adapter: Adapter<*>) : LifecycleObserver {
    var _emptyView = WeakReference(emptyView)
    var _adapter = WeakReference(adapter)


    private var observer: RecyclerView.AdapterDataObserver? = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            _emptyView.get()?.visibility = if (_adapter.get()?.itemCount == 0) View.VISIBLE else View.GONE
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            onChanged()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            onChanged()
        }
    }

    init {
        adapter.registerAdapterDataObserver(observer!!)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        _adapter.get()?.unregisterAdapterDataObserver(observer!!)
        observer = null
    }
}
