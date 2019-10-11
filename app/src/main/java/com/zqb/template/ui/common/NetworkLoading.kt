package com.zqb.template.ui.common

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.zqb.template.resource.Resource
import com.zqb.template.resource.Status

/**
 * 网络接口-loading接口
 */
interface NetworkLoading {

    /**
     * onResponse
     */
    fun <T> loading(resource: Resource<T>)
}

fun <T> Fragment.onResponse(resource: Resource<T>?): Boolean {
    if (resource != null) {
        val activity = this.activity
        if (activity is NetworkLoading) {
            activity.loading(resource)
        }

        resource.message?.let {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        }

        return resource.status == Status.SUCCESS && resource.data != null
    } else {
        return false
    }
}