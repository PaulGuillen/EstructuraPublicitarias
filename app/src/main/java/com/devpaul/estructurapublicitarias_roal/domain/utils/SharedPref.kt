package com.devpaul.estructurapublicitarias_roal.domain.utils

import android.content.Context
import android.content.SharedPreferences
import com.devpaul.estructurapublicitarias_roal.data.models.entity.Options
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPref(context: Context) {

    private var prefs: SharedPreferences? = null

    private val gson = Gson()

    init {
        prefs = context.getSharedPreferences("com.devpaul.roal", Context.MODE_PRIVATE)
    }

    fun saveData(key: String, value: String) {
        val prefsEditor: SharedPreferences.Editor? = prefs?.edit()
        prefsEditor?.putString(key, value)
        prefsEditor?.apply()
    }

    fun getData(key: String): String? {
        return prefs?.getString(key, "")
    }

    fun saveJsonObject(key: String, jsonObject: Any) {
        val editor = prefs?.edit()
        val json = gson.toJson(jsonObject)
        editor?.putString(key, json)
        editor?.apply()
    }

    fun getJsonObject(key: String): Any {
        val jsonString = prefs?.getString(key, "")
        val gson = Gson()
        return if (jsonString != "") {
            gson.fromJson(jsonString, object : TypeToken<Any>() {}.type)
        } else {
            Any()
        }
    }

    fun saveJsonListOptions(key: String, optionsList: List<Options>) {
        val editor = prefs?.edit()
        val json = gson.toJson(optionsList)
        editor?.putString(key, json)
        editor?.apply()
    }

    fun getJsonListOptions(key: String): List<Options>? {
        val jsonString = prefs?.getString(key, null)
        return if (!jsonString.isNullOrEmpty()) {
            val listType = object : TypeToken<List<Options>>() {}.type
            gson.fromJson(jsonString, listType)
        } else {
            emptyList()
        }
    }

    fun remove(key: String) {
        prefs?.edit()?.remove(key)?.apply()
    }

    fun clearPreferences() {
        val prefsEditor: SharedPreferences.Editor? = prefs?.edit()
        prefsEditor?.clear()
        prefsEditor?.apply()
    }
}
