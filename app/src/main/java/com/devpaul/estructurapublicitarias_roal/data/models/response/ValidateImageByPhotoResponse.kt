package com.devpaul.estructurapublicitarias_roal.data.models.response

import com.google.gson.annotations.SerializedName

class ValidateImageByPhotoResponse {

    @SerializedName("dni")
    var dni: String? = null
    override fun toString(): String {
        return "ValidateImageByPhotoResponse(dni=$dni)"
    }

}