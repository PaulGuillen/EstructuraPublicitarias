package com.devpaul.estructurapublicitarias_roal.data.models.request

import com.google.gson.annotations.SerializedName

class ValidateImageByPhotoRequest(

    @SerializedName("photo")
    var photo: String? = null,

    @SerializedName("photo_format")
    var photoFormat: String? = null

) {
    override fun toString(): String {
        return "ValidateImageByPhotoRequest(photo=$photo, photoFormat=$photoFormat)"
    }

}