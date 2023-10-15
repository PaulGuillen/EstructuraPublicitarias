package com.devpaul.estructurapublicitarias_roal.domain.interfaces.repository

import com.devpaul.estructurapublicitarias_roal.data.models.ValidationEPP
import com.devpaul.estructurapublicitarias_roal.data.models.request.ValidationEPPRequest
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult

interface ValidationEPPRepositoryNetwork {

    fun validateImageEPP(validationEPP: ValidationEPPRequest): CustomResult<ValidationEPP>

}