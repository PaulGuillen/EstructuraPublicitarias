package com.devpaul.estructurapublicitarias_roal.domain.usecases

import android.content.Context
import com.devpaul.estructurapublicitarias_roal.data.models.ValidationEPP
import com.devpaul.estructurapublicitarias_roal.data.models.request.ValidationEPPRequest
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.interfaces.repository.ValidationEPPRepositoryNetwork
import com.devpaul.estructurapublicitarias_roal.domain.utils.SharedPref

class ValidationEPPUseCase(
    var context: Context,
    private val validationEPPRepository: ValidationEPPRepositoryNetwork
) {
    private val prefs = SharedPref(context)

    fun validateEPP(validationEPP: ValidationEPPRequest): CustomResult<ValidationEPP> {
        val validateEPP = validationEPPRepository.validateImageEPP(validationEPP)
        when (validateEPP) {
            is CustomResult.OnSuccess -> {
                saveValidateEPP(validateEPP.data)
            }

            else -> {
                saveValidateEPP(ValidationEPP())
            }
        }
        return validateEPP
    }
    private fun saveValidateEPP(validateEPP: ValidationEPP) {
        if (validateEPP.code == 200) {
            prefs.saveJsonObject("ValidateEPP", validateEPP)
        }
    }

}