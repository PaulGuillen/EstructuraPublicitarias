package com.devpaul.estructurapublicitarias_roal.providers

import com.devpaul.estructurapublicitarias_roal.data.api.ApiRoutes
import com.devpaul.estructurapublicitarias_roal.data.models.response.ResponseHttp
import com.devpaul.estructurapublicitarias_roal.data.models.response.WorkersResponse
import com.devpaul.estructurapublicitarias_roal.data.routes.ApiConfig
import retrofit2.Call

class WorkersProvider {

    private var workersRoutes: ApiConfig? = null

    init {
        val api = ApiRoutes()
        workersRoutes = api.getWorkersRoutes()
    }


    fun postWorkers(workerUser: WorkersResponse): Call<ResponseHttp>? {
        return workersRoutes?.creatingWorkers(workerUser)
    }

    fun deleteWorker(dni: String): Call<ResponseHttp>? {
        return workersRoutes?.deleteWorker(dni)
    }

}