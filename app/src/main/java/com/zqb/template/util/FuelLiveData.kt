package com.zqb.template.util


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.kittinunf.fuel.core.*
import com.github.kittinunf.fuel.core.deserializers.ByteArrayDeserializer
import com.github.kittinunf.fuel.core.deserializers.StringDeserializer
import com.github.kittinunf.fuel.gson.gsonDeserializer
import com.github.kittinunf.result.Result
import com.zqb.template.resource.Resource
import java.nio.charset.Charset

/**
 * Created by Ihor Kucherenko on 01.06.17.
 * https://github.com/KucherenkoIhor
 */
fun Request.liveDataResponse() = liveDataResponse(ByteArrayDeserializer())

fun Request.liveDataResponseString(charset: Charset = Charsets.UTF_8) =
    liveDataResponse(StringDeserializer(charset))

inline fun <reified T : Any> Request.liveDataGsonResponse() =
    liveDataResponse(gsonDeserializer<T>())

inline fun <reified T : Any> Request.liveDataResponseObject(deserializable: Deserializable<T>) =
    liveDataResponse(deserializable)


inline fun <reified T : Any> Request.liveDataResponse(deserializable: Deserializable<T>): LiveData<Resource<T>> {
    val liveData = MutableLiveData<Resource<T>>()
    executionOptions.callbackExecutor.execute { liveData.value = Resource.loading(null) }
    val handler: (Request, Response, Result<T, FuelError>) -> Unit = { _, _, result ->
        when (result) {
            is Result.Success -> liveData.value = Resource.success(result.get())
            is Result.Failure -> liveData.value =
                Resource.error(result.error.localizedMessage, null)
        }
    }
    response(deserializable, handler)
    return liveData
}
