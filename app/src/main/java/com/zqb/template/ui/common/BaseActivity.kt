package com.zqb.template.ui.common

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.zqb.template.BR
import com.zqb.template.resource.Resource

abstract class BaseActivity : AppCompatActivity(), NetworkLoading {

    override fun <T> loading(resource: Resource<T>) {
        try {
            getBinding().setVariable(BR.resource, resource)
        } catch (e: Exception) {
        }
        if (!resource.message.isNullOrEmpty()) {
            Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
        }
    }

    abstract fun getBinding(): ViewDataBinding
}