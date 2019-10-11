package com.zqb.template.ui.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.zqb.template.R
import com.zqb.template.databinding.ActivityMainBinding
import com.zqb.template.model.MainModel
import com.zqb.template.ui.common.BaseActivity
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {
    private lateinit var _binding: ActivityMainBinding

    override fun getBinding() = _binding
    private val viewModel by viewModel<MainModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val adapter = TextPagingAdapter { viewModel.retry() }

        viewModel.pagedList.observe(this, Observer {
            adapter.submitList(it)
        })
        viewModel.networkState.observe(this, Observer {
            adapter.setNetworkState(it)
        })
        _binding.recycler.adapter = adapter
    }
}
