package com.devpaul.estructurapublicitarias_roal.data.models.request

import com.google.gson.annotations.SerializedName

class ValidationEPPRequest {

    @SerializedName("photo")
    var photo: String? = null

    override fun toString(): String {
        return "ValidationEPPRequest(photo=$photo)"
    }

}