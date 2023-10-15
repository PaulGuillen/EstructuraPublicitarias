package com.devpaul.estructurapublicitarias_roal.domain.mappers

import com.devpaul.estructurapublicitarias_roal.data.models.ValidationEPP
import com.devpaul.estructurapublicitarias_roal.data.models.response.ValidationEPPResponse

class ValidationEPPMapper {

    fun map(validationEPPResponse: ValidationEPPResponse): ValidationEPP {

        val validationEPP = ValidationEPP()

        validationEPP.code = validationEPPResponse.code
        validationEPP.message = validationEPPResponse.message
        validationEPP.requiredEquipment = validationEPPResponse.requiredEquipment.toString()

        return validationEPP
    }

}