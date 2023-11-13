package com.devpaul.estructurapublicitarias_roal.data.repository

import com.devpaul.estructurapublicitarias_roal.data.api.ApiRoutes
import com.devpaul.estructurapublicitarias_roal.data.models.entity.GeneralHTTP
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomNotFoundError
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.HttpError
import com.devpaul.estructurapublicitarias_roal.domain.mappers.GeneralHTTPMapper
import com.devpaul.estructurapublicitarias_roal.domain.interfaces.repository.LoginRepositoryNetwork
import com.devpaul.estructurapublicitarias_roal.data.models.response.MainUser
import com.devpaul.estructurapublicitarias_roal.data.models.response.ResponseHttp
import com.devpaul.estructurapublicitarias_roal.data.routes.ApiConfig
import com.devpaul.estructurapublicitarias_roal.domain.utils.TITLE_ERROR_MS_GET_MAIN_USER

class LoginRepository : LoginRepositoryNetwork {

    private var apiConfig: ApiConfig? = null
    private var messageTimeOut = "Time Out"

    init {
        val api = ApiRoutes()
        apiConfig = api.getWorkersRoutes()
    }
    override fun getMainUser(mainUser: MainUser): CustomResult<GeneralHTTP> {
        val serviceTitle = TITLE_ERROR_MS_GET_MAIN_USER
        try {
            val callApi = apiConfig?.mainUserEndPoints(mainUser)?.execute()

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
                    subtitle = messageTimeOut,
                )
            )
        }
    }


    override fun forgotPassword(mainUser: MainUser): CustomResult<GeneralHTTP> {
        val serviceTitle = "Error en el MS recoveryPassword"
        try {
            val callApi = apiConfig?.mainUserEndPoints(mainUser)?.execute()
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
                    subtitle = messageTimeOut,
                )
            )
        }
    }

    override fun codeVerification(mainUser: MainUser): CustomResult<GeneralHTTP> {
        val serviceTitle = "Error en el MS codeVerification"
        try {
            val callApi = apiConfig?.mainUserEndPoints(mainUser)?.execute()
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
                    subtitle = messageTimeOut,
                )
            )
        }
    }

    override fun changePassword(mainUser: MainUser): CustomResult<GeneralHTTP> {
        val serviceTitle = "Error en el MS changePassword"
        try {
            val callApi = apiConfig?.mainUserEndPoints(mainUser)?.execute()
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
                    subtitle = messageTimeOut,
                )
            )
        }
    }

}