package com.zqb.template.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.zqb.template.util.autoCleared

abstract class AutoClearedFragment<T : ViewDataBinding> : Fragment() {
    var binding by autoCleared<T>()
    var created = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!created) {
            binding = createView(inflater, container)
        }
        return binding.root
    }

    abstract fun createView(inflater: LayoutInflater, container: ViewGroup?): T

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (!created) {
            subscribeData()
            subscribeAction()
        }
        created = true
    }

    /**
     * viewModel 订阅UI
     */
    open fun subscribeData() {}

    /**
     * 订阅 view 事件
     */
    open fun subscribeAction() {}
}