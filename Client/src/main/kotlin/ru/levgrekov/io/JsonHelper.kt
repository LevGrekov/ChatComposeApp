package ru.levgrekov.io

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object JsonHelper {
    private val gson = Gson()
    private inline fun <reified T> Gson.fromJson(json: String): T =
        this.fromJson<T>(json, object: TypeToken<T>() {}.type)

    fun toJSON(vararg keyValue: Pair<String,Any?>): String {
        mutableMapOf<String, Any?>().apply {
            keyValue.forEach { this[it.first] = it.second }
            return gson.toJson(this)
        }
    }

    fun getMapFromJson(json: String): Map<String, Any?> =
        gson.fromJson<Map<String, Any?>>(json)

}