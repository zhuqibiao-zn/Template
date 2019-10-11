package com.zqb.template

import android.app.Application
import com.github.kittinunf.fuel.core.FuelManager
import com.zqb.template.model.viewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber
import javax.net.ssl.HostnameVerifier

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        startKoin {
            androidLogger()
            androidContext(this@MyApplication)

            modules(arrayListOf(viewModel))
        }

        initFuel()
    }

    /**
     * 初始化网络模块
     */
    private fun initFuel() {
        FuelManager.instance.apply {
            addRequestInterceptor {
                { req ->
                    val parameters = StringBuffer()
                    req.parameters.forEach {
                        parameters.append("${it.first}=${it.second}")
                    }
                    Timber.d("Request: ${req.method} ${req.url}  $parameters")
                    if (!req.body.isEmpty()) {
                        Timber.d(req.body.asString("application/json"))
                    }
                    it(req)
                }
            }

            addResponseInterceptor {
                { req, resp ->
                    if (resp.statusCode < 200 || resp.statusCode > 300) {
                        Timber.e("${req.method}  ${resp.statusCode} ${resp.url} ${resp.responseMessage}")
                    } else {
                        Timber.d("Response：${req.method}  ${resp.statusCode} ${resp.url} ")
                        if (!resp.body().isEmpty()) {
                            Timber.d(resp.body().asString("application/json"))
                        }
                    }
                    it(req, resp)
                }
            }

            hostnameVerifier = HostnameVerifier { hostname, session ->
                true
            }

            basePath = BuildConfig.HOST
        }
    }
}