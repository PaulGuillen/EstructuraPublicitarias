package com.devpaul.estructurapublicitarias_roal.data.repository

import com.devpaul.estructurapublicitarias_roal.data.api.ApiRoutes
import com.devpaul.estructurapublicitarias_roal.data.models.entity.PrincipalListWorker
import com.devpaul.estructurapublicitarias_roal.data.models.entity.WorkerReportByUser
import com.devpaul.estructurapublicitarias_roal.data.models.response.PrincipalListWorkerResponse
import com.devpaul.estructurapublicitarias_roal.data.models.response.WorkerReportByUserResponse
import com.devpaul.estructurapublicitarias_roal.data.routes.ApiConfig
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomNotFoundError
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.HttpError
import com.devpaul.estructurapublicitarias_roal.domain.interfaces.repository.WorkersReportRepositoryNetwork
import com.devpaul.estructurapublicitarias_roal.domain.mappers.WorkerReportMapper
import com.devpaul.estructurapublicitarias_roal.domain.utils.*

class WorkersReportRepository : WorkersReportRepositoryNetwork {

    private var apiConfig: ApiConfig? = null

    init {
        val api = ApiRoutes()
        apiConfig = api.getWorkersRoutes()
    }

    override fun allWorkers(): CustomResult<List<PrincipalListWorker>> {

        val serviceTitle = TITLE_ERROR_MS_ALL_WORKERS

        try {
            val callApi = apiConfig?.allWorkers()?.execute()

            return when (callApi?.isSuccessful) {
                true -> {
                    val response: List<PrincipalListWorkerResponse>? = callApi.body()

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
                    subtitle = MESSAGE_TO
                )
            )
        }
    }

    override fun reportByWorker(document: String): CustomResult<WorkerReportByUser> {

        val serviceTitle = TITLE_ERROR_MS_DATA_REPORT_WORKER

        try {
            val callApi = apiConfig?.reportWorker(document)?.execute()

            return when (callApi?.isSuccessful) {
                true -> {
                    val response: WorkerReportByUserResponse? = callApi.body()

                    if (response != null)
                        CustomResult.OnSuccess(WorkerReportMapper().mapReportData(response))
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
                    subtitle = ex.toString()
                )
            )
        }
    }

}