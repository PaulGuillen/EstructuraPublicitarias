package com.devpaul.estructurapublicitarias_roal.data.repository

import com.devpaul.estructurapublicitarias_roal.data.models.response.GetWorkerResponse
import com.devpaul.estructurapublicitarias_roal.data.api.ApiRoutes
import com.devpaul.estructurapublicitarias_roal.data.models.entity.ListWorker
import com.devpaul.estructurapublicitarias_roal.data.models.entity.GeneralHTTP
import com.devpaul.estructurapublicitarias_roal.data.models.entity.GetWorker
import com.devpaul.estructurapublicitarias_roal.data.models.entity.Options
import com.devpaul.estructurapublicitarias_roal.data.models.entity.ValidateImageByPhoto
import com.devpaul.estructurapublicitarias_roal.data.models.request.ValidateImageByPhotoRequest
import com.devpaul.estructurapublicitarias_roal.data.models.request.WorkerRequest
import com.devpaul.estructurapublicitarias_roal.data.models.response.ListWorkerResponse
import com.devpaul.estructurapublicitarias_roal.data.models.response.OptionsResponse
import com.devpaul.estructurapublicitarias_roal.data.models.response.ResponseHttp
import com.devpaul.estructurapublicitarias_roal.data.models.response.ValidateImageByPhotoResponse
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomNotFoundError
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.HttpError
import com.devpaul.estructurapublicitarias_roal.domain.mappers.WorkerMapper
import com.devpaul.estructurapublicitarias_roal.domain.interfaces.repository.WorkersRepositoryNetwork
import com.devpaul.estructurapublicitarias_roal.data.routes.ApiConfig
import com.devpaul.estructurapublicitarias_roal.domain.mappers.AllWorkersMapper
import com.devpaul.estructurapublicitarias_roal.domain.mappers.GeneralHTTPMapper
import com.devpaul.estructurapublicitarias_roal.domain.mappers.OptionsMapper
import com.devpaul.estructurapublicitarias_roal.domain.mappers.ValidateImageByPhotoMapper
import com.devpaul.estructurapublicitarias_roal.domain.utils.*

class WorkersRepository : WorkersRepositoryNetwork {

    private var apiConfig: ApiConfig? = null

    init {
        val api = ApiRoutes()
        apiConfig = api.getWorkersRoutes()
    }


    override fun getWorkers(dni: String): CustomResult<GetWorker> {

        val serviceTitle = TITLE_ERROR_MS_GET_WORKERS

        try {
            val callApi = apiConfig?.getWorkers(dni)?.execute()

            return when (callApi?.isSuccessful) {
                true -> {
                    val response: GetWorkerResponse? = callApi.body()

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

    override fun createWorker(workerRequest: WorkerRequest): CustomResult<GeneralHTTP> {

        val serviceTitle = TITLE_ERROR_MS_CREATE_WORKER

        try {
            val callApi = apiConfig?.createWorker(workerRequest)?.execute()

            return when (callApi?.isSuccessful) {
                true -> {
                    val response: ResponseHttp? = callApi.body()

                    if (response != null)
                        CustomResult.OnSuccess(GeneralHTTPMapper().map(response))
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

    override fun deleteWorker(dni: String): CustomResult<GeneralHTTP> {

        val serviceTitle = TITLE_ERROR_MS_DELETE_WORKER

        try {
            val callApi = apiConfig?.deleteWorker(dni)?.execute()

            return when (callApi?.isSuccessful) {
                true -> {
                    val response: ResponseHttp? = callApi.body()

                    if (response != null)
                        CustomResult.OnSuccess(GeneralHTTPMapper().map(response))
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

    override fun listWorkers(): CustomResult<ListWorker> {

        val serviceTitle = TITLE_ERROR_MS_LIST_WORKERS

        try {
            val callApi = apiConfig?.listWorkers()?.execute()

            return when (callApi?.isSuccessful) {
                true -> {
                    val response: ListWorkerResponse? = callApi.body()

                    if (response != null)
                        CustomResult.OnSuccess(AllWorkersMapper().map(response))
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

}