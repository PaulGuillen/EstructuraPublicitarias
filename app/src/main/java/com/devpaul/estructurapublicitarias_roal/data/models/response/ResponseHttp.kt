package com.devpaul.estructurapublicitarias_roal.data.models.response

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class ResponseHttp(
    @SerializedName("isSuccess")
    val isSuccess: String? = null,

    @SerializedName("message")
    val message: String,

    @SerializedName("code")
    val code: Int,

    @SerializedName("data")
    val data: JsonObject,

    @SerializedName("error")
    val error: String

) {
    override fun toString(): String {
        return "ResponseHttp(isSuccess=$isSuccess, message='$message', code=$code, data=$data, error='$error')"
    }
}