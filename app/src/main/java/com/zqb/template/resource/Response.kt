package com.zqb.template.resource

/**
 * 响应 -状态-抽象
 */
interface ResponseStatus {
    fun success(): Boolean

    fun errorMsg(): String {
        return "服务异常"
    }
}

/**
 * 分页查询 响应数据 关键字：result: ArrayList<Type>?
 */
data class PageResponse<Type>(
    val result: ArrayList<Type>?,
    val respId: String = "",
    val responseCode: Int = 0,
    val msg: String?
) : ResponseStatus {
    override fun success(): Boolean {
        return responseCode == 0
    }

    override fun errorMsg(): String {
        return msg ?: "服务异常！"
    }
}