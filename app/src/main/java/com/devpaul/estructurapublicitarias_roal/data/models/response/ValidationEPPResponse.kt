package com.devpaul.estructurapublicitarias_roal.data.models.response

import com.google.gson.annotations.SerializedName

data class ValidationEPPResponse(
    @SerializedName("code")
    val code: Int? = null,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("equipo_requerido")
    val requiredEquipment: List<String>? = null

) {
    override fun toString(): String {
        return "ValidationEPPResponse(code=$code, message=$message, requiredEquipment=$requiredEquipment)"
    }
}