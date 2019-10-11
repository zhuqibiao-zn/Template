package com.zqb.template.resource

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.result.Result

/**
 * 通用分页（数字页码）-加载 数据源
 */
abstract class CommonDataSource<Value> : PageKeyedDataSource<Int, Value>() {
    // keep a function reference for the retry event
    private var retry: (() -> Any)? = null
    /**
     * There is no sync on the state because paging will always call loadInitial first then wait
     * for it to return some success value before calling loadAfter.
     */
    val networkState = MediatorLiveData<Resource<Any>>()

    val initialLoad = MutableLiveData<Resource<Any>>()

    fun retryFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.invoke()
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Value>
    ) {
        networkState.postValue(Resource.success(null))
        initialLoad.postValue(Resource.loading())
        createCall(1).responseObject<PageResponse<Value>> { _, _, result ->
            when (result) {
                is Result.Success -> {
                    result.value.apply {
                        if (success()) {
                            networkState.postValue(Resource.success(null))
                            initialLoad.postValue(Resource.success(null))
                            callback.onResult(this.result ?: arrayListOf(), 0, 2)
                        } else {
                            retry = { loadInitial(params, callback) }
                            networkState.postValue(Resource.error(errorMsg()))
                            initialLoad.postValue(Resource.error(errorMsg()))
                        }
                    }

                }
                is Result.Failure -> {
                    retry = { loadInitial(params, callback) }
                    networkState.postValue(Resource.error(result.error.localizedMessage))
                    initialLoad.postValue(Resource.error(result.error.localizedMessage))
                }
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Value>) {
        networkState.postValue(Resource.loading())
        createCall(params.key).responseObject<PageResponse<Value>> { _, _, result ->
            when (result) {
                is Result.Success -> {
                    if (result.value.success()) {
                        networkState.postValue(Resource.success(null))
                        callback.onResult(result.value.result ?: emptyList(), params.key + 1)
                    } else {
                        retry = { loadAfter(params, callback) }
                        networkState.postValue(Resource.error(result.value.errorMsg()))
                    }
                }
                is Result.Failure -> {
                    retry = { loadAfter(params, callback) }
                    networkState.postValue(Resource.error(result.error.localizedMessage))
                }
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Value>) {
    }

    abstract fun createCall(page: Int): Request
}