package com.devpaul.estructurapublicitarias_roal.data.repository

import com.devpaul.estructurapublicitarias_roal.data.api.ApiRoutes
import com.devpaul.estructurapublicitarias_roal.data.models.entity.PrincipalListWorker
import com.devpaul.estructurapublicitarias_roal.data.models.response.PrincipalListWorkerResponse
import com.devpaul.estructurapublicitarias_roal.data.routes.ApiConfig
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomNotFoundError
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.HttpError
import com.devpaul.estructurapublicitarias_roal.domain.interfaces.repository.WorkersReportRepositoryNetwork
import com.devpaul.estructurapublicitarias_roal.domain.mappers.WorkerReportMapper
import com.devpaul.estructurapublicitarias_roal.domain.utils.TITLE_ERROR_MS_ALL_WORKERS

class WorkersReportRepository : WorkersReportRepositoryNetwork {

    private var apiConfig: ApiConfig? = null

    init {
        val api = ApiRoutes()
        apiConfig = api.getWorkersRoutes()
    }

    override fun allWorkers(): CustomResult<PrincipalListWorker> {

        val serviceTitle = TITLE_ERROR_MS_ALL_WORKERS

        try {
            val callApi = apiConfig?.allWorkers()?.execute()

            return when (callApi?.isSuccessful) {
                true -> {
                    val response: PrincipalListWorkerResponse? = callApi.body()

                    if (response != null)
                        CustomResult.OnSuccess(WorkerReportMapper().map(response))
                    else {
                        CustomResult.OnError(CustomNotFoundError())
                    }
                }

                false -> {
                    CustomResult.OnError(
                        HttpError(
                            code = callApi.code(),
                            title = serviceTitle,
                            subtitle = callApi.message()
                        )
                    )
                }

                else -> {
                    CustomResult.OnError(
                        HttpError(
                            code = callApi?.code(),
                            title = serviceTitle,
                            subtitle = callApi?.message()
                        )
                    )
                }
            }

        } catch (ex: Exception) {
            return CustomResult.OnError(
                HttpError(
                    code = 408,
                    title = serviceTitle,
                    subtitle = TITLE_ERROR_MS_ALL_WORKERS
                )
            )
        }
    }

}