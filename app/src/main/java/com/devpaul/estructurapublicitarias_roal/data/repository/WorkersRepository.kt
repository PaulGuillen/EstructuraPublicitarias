package com.devpaul.estructurapublicitarias_roal.data.repository

import com.devpaul.estructurapublicitarias_roal.data.api.ApiRoutes
import com.devpaul.estructurapublicitarias_roal.data.models.Options
import com.devpaul.estructurapublicitarias_roal.data.models.Worker
import com.devpaul.estructurapublicitarias_roal.data.models.response.OptionsResponse
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomNotFoundError
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.HttpError
import com.devpaul.estructurapublicitarias_roal.domain.mappers.WorkerMapper
import com.devpaul.estructurapublicitarias_roal.domain.interfaces.repository.WorkersRepositoryNetwork
import com.devpaul.estructurapublicitarias_roal.data.models.response.WorkersResponse
import com.devpaul.estructurapublicitarias_roal.data.routes.ApiConfig
import com.devpaul.estructurapublicitarias_roal.domain.mappers.OptionsMapper

class WorkersRepository : WorkersRepositoryNetwork {

    private var apiConfig: ApiConfig? = null

    init {
        val api = ApiRoutes()
        apiConfig = api.getWorkersRoutes()
    }

    override fun getWorkers(dni: String): CustomResult<Worker> {
        try {
            val callApi = apiConfig?.getWorkers(dni)?.execute()

            return when (callApi?.isSuccessful) {
                true -> {
                    val response: WorkersResponse? = callApi.body()

                    if (response != null)
                        CustomResult.OnSuccess(WorkerMapper().map(response))
                    else {
                        CustomResult.OnError(CustomNotFoundError())
                    }
                }

                false -> {
                    CustomResult.OnError(
                        HttpError(
                            code = callApi.code(),
                            title = "Error en el MS getWorkers",
                            subtitle = callApi.message()
                        )
                    )
                }

                else -> {
                    CustomResult.OnError(
                        HttpError(
                            code = callApi?.code(),
                            title = "Error en el MS getWorkers",
                            subtitle = callApi?.message()
                        )
                    )
                }
            }

        } catch (ex: Exception) {
            return CustomResult.OnError(
                HttpError(
                    code = 408,
                    title = "Error en el MS getWorkers",
                    subtitle = "En este momento el servicio no está disponible"
                )
            )
        }
    }

    override fun getOptionList(): CustomResult<List<Options>> {
        try {
            val callApi = apiConfig?.getOptions()?.execute()

            return when (callApi?.isSuccessful) {
                true -> {
                    val response: List<OptionsResponse>? = callApi.body()

                    if (response != null)
                        CustomResult.OnSuccess(OptionsMapper().map(response))
                    else {
                        CustomResult.OnError(CustomNotFoundError())
                    }
                }

                false -> {
                    CustomResult.OnError(
                        HttpError(
                            code = callApi.code(),
                            title = "Error en el MS getOptions"
                        )
                    )
                }

                else -> {
                    CustomResult.OnError(
                        HttpError(
                            code = callApi?.code(),
                            title = "Error en el MS getOptions"
                        )
                    )
                }
            }

        } catch (ex: Exception) {
            return CustomResult.OnError(
                HttpError(
                    code = 408,
                    title = "En este momento el servicio no está disponible"
                )
            )
        }
    }
}