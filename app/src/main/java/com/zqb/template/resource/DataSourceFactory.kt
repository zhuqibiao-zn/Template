package com.zqb.template.resource

import androidx.paging.DataSource
import androidx.paging.toLiveData
import com.github.kittinunf.fuel.core.Request

/**
 * Paging分页加载 Factory
 */
abstract class DataSourceFactory<Value> constructor(private val pageSize: Int = 10) :
    DataSource.Factory<Int, Value>() {
    val source = object : CommonDataSource<Value>() {
        override fun createCall(page: Int): Request {
            return this@DataSourceFactory.createCall(page)
        }
    }

    override fun create(): DataSource<Int, Value> {
        return source
    }

    abstract fun createCall(page: Int): Request

    fun asListing(): Listing<Value> =
        Listing(
            toLiveData(pageSize),
            source.networkState,
            source.initialLoad,
            { source.invalidate() },
            { source.retryFailed() })
}