package com.devpaul.estructurapublicitarias_roal.domain.utils

import android.content.Context
import android.content.SharedPreferences
import com.devpaul.estructurapublicitarias_roal.data.models.Worker
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

    fun saveWorker(key: String, value: Worker) {
        val editor = prefs?.edit()
        val json = Gson().toJson(value)
        editor?.putString(key, json)
        editor?.apply()
    }

    fun getWorker(key: String): Worker {

        val jsonString = prefs?.getString(key, "")
        val gson = Gson()
        if (jsonString != "") {
            return gson.fromJson(jsonString, object : TypeToken<Worker>() {}.type)
        } else {
            return Worker()
        }
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

    fun remove(key: String) {
        prefs?.edit()?.remove(key)?.apply()
    }

    fun clearPreferences() {
        val prefsEditor: SharedPreferences.Editor? = prefs?.edit()
        prefsEditor?.clear()
        prefsEditor?.apply()
    }
}
