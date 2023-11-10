package com.devpaul.estructurapublicitarias_roal.domain.utils

class SingletonData private constructor() {

    private val dataMap: MutableMap<String, String> = mutableMapOf()

    companion object {
        @Volatile
        private var instance: SingletonData? = null

        fun getInstance(): SingletonData {
            return instance ?: synchronized(this) {
                instance ?: SingletonData().also { instance = it }
            }
        }
    }

    fun setData(key: String, value: String) {
        dataMap[key] = value
    }

    fun getData(key: String, defaultValue: String? = null): String? {
        return dataMap[key] ?: defaultValue
    }

    fun getAllData(): Map<String, String> {
        return dataMap.toMap()
    }

    fun removeData(key: String) {
        dataMap.remove(key)
    }

    fun clearSingletonData() {
        dataMap.clear()
    }
}
