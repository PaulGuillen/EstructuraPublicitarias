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

class ValidationEPPRepository : ValidationEPPRepositoryNetwork{

    private var apiConfig: ApiConfig? = null
    private var messageTimeOut = "Time Out"

    init {
        val api = ApiRoutes()
        apiConfig = api.getWorkersRoutes()
    }

    override fun validateImageEPP(validationEPP: ValidationEPPRequest): CustomResult<ValidationEPP> {

        val serviceTitle = "Error en el MS validateImageEPP"

        try {
            val callApi = apiConfig?.validationEPP(validationEPP)?.execute()

            return when (callApi?.isSuccessful) {
                true -> {
                    val response: ValidationEPPResponse? = callApi.body()

                    if (response != null)
                        CustomResult.OnSuccess(ValidationEPPMapper().map(response))
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