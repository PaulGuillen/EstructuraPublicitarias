package com.devpaul.estructurapublicitarias_roal.data.api

import com.devpaul.estructurapublicitarias_roal.BuildConfig
import com.devpaul.estructurapublicitarias_roal.data.routes.UsersRoutes
import com.devpaul.estructurapublicitarias_roal.data.routes.ApiConfig

class ApiRoutes {


    private val retrofit = RetrofitClient()

    fun getMainUserRoutes(): UsersRoutes {
        return retrofit.getClient(BuildConfig.BASE_URL).create(UsersRoutes::class.java)
    }

    fun getWorkersRoutes(): ApiConfig {
        return retrofit.getClient(BuildConfig.BASE_URL).create(ApiConfig::class.java)
    }
}