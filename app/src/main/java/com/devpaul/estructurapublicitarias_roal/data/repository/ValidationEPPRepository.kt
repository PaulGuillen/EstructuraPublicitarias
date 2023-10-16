package com.devpaul.estructurapublicitarias_roal.data.repository

import com.devpaul.estructurapublicitarias_roal.data.api.ApiRoutes
import com.devpaul.estructurapublicitarias_roal.data.models.ValidationEPP
import com.devpaul.estructurapublicitarias_roal.data.models.request.ValidationEPPRequest
import com.devpaul.estructurapublicitarias_roal.data.models.response.ValidationEPPResponse
import com.devpaul.estructurapublicitarias_roal.data.routes.ApiConfig
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomNotFoundError
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.HttpError
import com.devpaul.estructurapublicitarias_roal.domain.interfaces.repository.ValidationEPPRepositoryNetwork
import com.devpaul.estructurapublicitarias_roal.domain.mappers.ValidationEPPMapper
import com.google.gson.Gson

class ValidationEPPRepository : ValidationEPPRepositoryNetwork {

    private var apiConfig: ApiConfig? = null
    private var messageTimeOut = "Time Out"

    init {
        val api = ApiRoutes()
        apiConfig = api.getWorkersRoutes()
    }

    override fun validateImageEPP(validationEPP: ValidationEPPRequest): CustomResult<ValidationEPP> {

        val serviceTitle = "Error en el MS validate EPP"

        try {

            val callApi = apiConfig?.validationEPP(validationEPP)?.execute()
            val response: ValidationEPPResponse? = callApi?.body()

            return when (callApi?.isSuccessful) {
                true -> {
                    if (response != null)
                        CustomResult.OnSuccess(ValidationEPPMapper().map(response))
                    else {
                        CustomResult.OnError(CustomNotFoundError())
                    }

                }

                false -> {
                    val errorBody = callApi.errorBody()?.string()
                    val gson = Gson()
                    val errorResponse = gson.fromJson(errorBody, ValidationEPPResponse::class.java)

                    CustomResult.OnError(
                        HttpError(
                            code = callApi.code(),
                            title = serviceTitle,
                            subtitle = errorResponse.message
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