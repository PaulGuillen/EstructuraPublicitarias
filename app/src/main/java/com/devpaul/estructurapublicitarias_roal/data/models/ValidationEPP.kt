package com.devpaul.estructurapublicitarias_roal.data.models

data class ValidationEPP(
    var code: Int? = null,
    var message: String? = null,
    var area: String? = null,
    var requiredEquipment: List<EquipmentItem>? = null
)

data class EquipmentItem(
    var key: String? = null,
    var value: Boolean? = null
)
