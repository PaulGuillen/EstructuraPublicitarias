package com.devpaul.estructurapublicitarias_roal.data.models.entity

data class ValidationEPP(
    var code: Int? = null,
    var message: String? = null,
    var area: String? = null,
    var allEquipment: List<AllEquipment>? = null,
    var wearingEquipment: List<EquipmentItem>? = null,
    var descriptionEquipment: String? = null
)

data class AllEquipment(
    var key: String? = null,
    var value: String? = null
)

data class EquipmentItem(
    var key: String? = null,
    var value: Boolean? = null
)
