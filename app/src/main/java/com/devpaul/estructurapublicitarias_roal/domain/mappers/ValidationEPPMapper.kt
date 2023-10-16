package com.devpaul.estructurapublicitarias_roal.domain.mappers

import com.devpaul.estructurapublicitarias_roal.data.models.EquipmentItem
import com.devpaul.estructurapublicitarias_roal.data.models.ValidationEPP
import com.devpaul.estructurapublicitarias_roal.data.models.response.ValidationEPPResponse

class ValidationEPPMapper {
    fun map(validationEPPResponse: ValidationEPPResponse): ValidationEPP {
        val validationEPP = ValidationEPP()

        validationEPP.code = validationEPPResponse.code
        validationEPP.message = validationEPPResponse.message
        validationEPP.area = validationEPPResponse.area

        validationEPP.requiredEquipment = validationEPPResponse.requiredEquipment?.map { item ->
            EquipmentItem(
                key = item.key,
                value = item.value
            )
        }

        return validationEPP
    }
}
