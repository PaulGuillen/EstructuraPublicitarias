package com.devpaul.estructurapublicitarias_roal.data.models.response

import com.google.gson.annotations.SerializedName

class ValidateImageByPhotoResponse(
    @SerializedName("code")
    var code: Int? = null,
    @SerializedName("dni")
    var dni: String? = null
) {
    override fun toString(): String {
        return "ValidateImageByPhotoResponse(code=$code, dni=$dni)"
    }
}