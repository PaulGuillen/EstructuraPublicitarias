package com.devpaul.estructurapublicitarias_roal.data.models.response

import com.google.gson.annotations.SerializedName

data class ValidationEPPResponse(
    @SerializedName("code")
    val code: Int? = null,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("area")
    val area: String? = null,

    @SerializedName("allEquipment")
    val allEquipment: List<AllEquipment>? = null,

    @SerializedName("wearingEquipment")
    val wearingEquipment: List<EquipmentItem>? = null

) {
    override fun toString(): String {
        return "ValidationEPPResponse(code=$code, message=$message, area=$area, allEquipment=$allEquipment, wearingEquipment=$wearingEquipment)"
    }

}

data class AllEquipment(
    @SerializedName("key")
    val key: String? = null,

    @SerializedName("value")
    val value: String? = null
)

data class EquipmentItem(
    @SerializedName("key")
    val key: String? = null,

    @SerializedName("value")
    val value: Boolean? = null
)
