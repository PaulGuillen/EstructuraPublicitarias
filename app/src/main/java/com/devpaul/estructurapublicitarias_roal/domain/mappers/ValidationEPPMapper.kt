package com.devpaul.estructurapublicitarias_roal.domain.mappers

import com.devpaul.estructurapublicitarias_roal.data.models.entity.AllEquipment
import com.devpaul.estructurapublicitarias_roal.data.models.entity.EquipmentItem
import com.devpaul.estructurapublicitarias_roal.data.models.entity.ValidationEPP
import com.devpaul.estructurapublicitarias_roal.data.models.response.ValidationEPPResponse

class ValidationEPPMapper {
    fun map(validationEPPResponse: ValidationEPPResponse): ValidationEPP {
        val validationEPP = ValidationEPP()

        validationEPP.code = validationEPPResponse.code
        validationEPP.message = validationEPPResponse.message
        validationEPP.area = validationEPPResponse.area

        validationEPP.allEquipment = validationEPPResponse.allEquipment?.map { item ->
            AllEquipment(
                key = item.key,
                value = item.value
            )
        }

        validationEPP.wearingEquipment = validationEPPResponse.wearingEquipment?.map { item ->
            EquipmentItem(
                key = item.key,
                value = item.value
            )
        }

        validationEPP.descriptionEquipment = validationEPPResponse.descriptionEquipment

        return validationEPP
    }
}
