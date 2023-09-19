package com.devpaul.estructurapublicitarias_roal.data.models.response

import com.google.gson.annotations.SerializedName

class OptionsResponse(

    @SerializedName("optionID")
    val optionID: String? = null,

    @SerializedName("name")
    val name: String ? = null,

    @SerializedName("description")
    val description: String ? = null,

    @SerializedName("image")
    val image: String ? = null,

)