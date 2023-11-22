package com.devpaul.estructurapublicitarias_roal.domain.usecases.validateEPP

import com.devpaul.estructurapublicitarias_roal.data.models.entity.ValidationEPP
import com.devpaul.estructurapublicitarias_roal.data.models.request.ValidationEPPRequest
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.interfaces.repository.ValidationEPPRepositoryNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ValidationEPPUseCase(private val validationEPPRepository: ValidationEPPRepositoryNetwork) {

    suspend fun validateEPP(validationEPP: ValidationEPPRequest): CustomResult<ValidationEPP> =
        withContext(Dispatchers.IO) {

            val validateEPP = validationEPPRepository.validateImageEPP(validationEPP)

            when (validateEPP) {
                is CustomResult.OnSuccess -> {
                    validateEPP.data
                }

                else -> {
                    ValidationEPP()
                }
            }
            return@withContext validateEPP
        }
}