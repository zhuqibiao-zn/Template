package com.zqb.template.db

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import org.koin.core.context.GlobalContext
import kotlin.reflect.KProperty

/**
 * SharedPreferences 包装类
 */
@Suppress("UNCHECKED_CAST")
class Preference<T>(val name: String, val default: T) {
    val prefs: SharedPreferences by lazy {
            GlobalContext.get().koin.get<Application>().getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    /**
     * 读取值
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return getValue(name, default)
    }

    /**
     * 写入值
     */
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putValue(name, value)
    }

    /**
     * 读取值
     */
    private fun getValue(name: String, default: T): T = with(prefs) {
        val res: Any = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> throw IllegalArgumentException("SharedPreferences can't be get this type")
        }
        res as T
    }

    /**
     * 写入值
     */
    private fun putValue(name: String, value: T) = with(prefs.edit()) {
        when (value) {
            null -> remove(name)
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> throw IllegalArgumentException("SharedPreferences can't be save this type")
        }.apply()
    }

    fun remove() = with(prefs.edit()) {
        remove(name)
    }
}