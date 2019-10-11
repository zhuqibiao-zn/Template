package com.zqb.template.repository

import com.github.kittinunf.fuel.Fuel
import com.zqb.template.resource.DataSourceFactory

class Repository {

    fun getList()= object : DataSourceFactory<String>() {
        override fun createCall(page: Int) = Fuel.get("appMachine/queryAppErpSell")
    }.asListing()
}