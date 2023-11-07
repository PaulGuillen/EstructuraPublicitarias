package com.devpaul.estructurapublicitarias_roal.data.repository

import com.devpaul.estructurapublicitarias_roal.data.api.ApiRoutes
import com.devpaul.estructurapublicitarias_roal.data.models.entity.Options
import com.devpaul.estructurapublicitarias_roal.data.models.entity.ValidateImageByPhoto
import com.devpaul.estructurapublicitarias_roal.data.models.entity.Worker
import com.devpaul.estructurapublicitarias_roal.data.models.request.ValidateImageByPhotoRequest
import com.devpaul.estructurapublicitarias_roal.data.models.response.OptionsResponse
import com.devpaul.estructurapublicitarias_roal.data.models.response.ValidateImageByPhotoResponse
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomNotFoundError
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.HttpError
import com.devpaul.estructurapublicitarias_roal.domain.mappers.WorkerMapper
import com.devpaul.estructurapublicitarias_roal.domain.interfaces.repository.WorkersRepositoryNetwork
import com.devpaul.estructurapublicitarias_roal.data.models.response.WorkersResponse
import com.devpaul.estructurapublicitarias_roal.data.routes.ApiConfig
import com.devpaul.estructurapublicitarias_roal.domain.mappers.OptionsMapper
import com.devpaul.estructurapublicitarias_roal.domain.mappers.ValidateImageByPhotoMapper
import com.devpaul.estructurapublicitarias_roal.domain.utils.*
class WorkersRepository : WorkersRepositoryNetwork {

    private var apiConfig: ApiConfig? = null

    init {
        val api = ApiRoutes()
        apiConfig = api.getWorkersRoutes()
    }

    override fun getWorkers(dni: String): CustomResult<Worker> {

        val serviceTitle = TITLE_ERROR_MS_GET_WORKERS

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
                    subtitle = SUBTITLE_MESSAGE_TIMEOUT_SERVICE
                )
            )
        }
    }

    override fun getOptionList(): CustomResult<List<Options>> {

        val serviceTitle = TITLE_ERROR_MS_GET_OPTIONS

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
                            title = serviceTitle
                        )
                    )
                }

                else -> {
                    CustomResult.OnError(
                        HttpError(
                            code = callApi?.code(),
                            title = serviceTitle
                        )
                    )
                }
            }

        } catch (ex: Exception) {
            return CustomResult.OnError(
                HttpError(
                    code = 408,
                    title = SUBTITLE_MESSAGE_TIMEOUT_SERVICE
                )
            )
        }
    }

    override fun validateImageByPhoto(validateImageByPhotoRequest: ValidateImageByPhotoRequest): CustomResult<ValidateImageByPhoto> {

        val serviceTitle = TITLE_ERROR_MS_VALIDATE_IMAGE_PHOTO

        try {
            val callApi = apiConfig?.validateImageByPhoto(validateImageByPhotoRequest)?.execute()

            return when (callApi?.isSuccessful) {
                true -> {
                    val response: ValidateImageByPhotoResponse? = callApi.body()

                    if (response != null)
                        CustomResult.OnSuccess(ValidateImageByPhotoMapper().map(response))
                    else {
                        CustomResult.OnError(CustomNotFoundError())
                    }
                }

                false -> {
                    CustomResult.OnError(
                        HttpError(
                            code = callApi.code(),
                            title = serviceTitle
                        )
                    )
                }

                else -> {
                    CustomResult.OnError(
                        HttpError(
                            code = callApi?.code(),
                            title = serviceTitle
                        )
                    )
                }
            }

        } catch (ex: Exception) {
            return CustomResult.OnError(
                HttpError(
                    code = 408,
                    title = SUBTITLE_MESSAGE_TIMEOUT_SERVICE
                )
            )
        }
    }

}