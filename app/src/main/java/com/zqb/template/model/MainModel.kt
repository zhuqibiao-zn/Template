package com.zqb.template.model

import androidx.lifecycle.ViewModel
import com.zqb.template.repository.Repository

class MainModel(private val repository: Repository) :ViewModel() {
    private val listing = repository.getList()
    val pagedList = listing.pagedList
    val networkState = listing.networkState
    val refreshState = listing.refreshState
    fun retry() = listing.retry.invoke()
}