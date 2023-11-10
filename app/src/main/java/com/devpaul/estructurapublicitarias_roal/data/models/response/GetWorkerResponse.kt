package com.devpaul.estructurapublicitarias_roal.data.models.response

import com.google.gson.annotations.SerializedName

class GetWorkerResponse(
    @SerializedName("isSuccess")
    val isSuccess: Boolean = false,

    @SerializedName("code")
    val code: Int = 0,

    @SerializedName("message")
    val message: MessageResponse? = null
)

class MessageResponse(
    @SerializedName("dni")
    val dni: String? = null,

    @SerializedName("date_birth")
    val dateBirth: String? = null,

    @SerializedName("date_join")
    val dateJoin: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("gender")
    val gender: String? = null,

    @SerializedName("phone")
    val phone: String? = null,

    @SerializedName("blood_type")
    val bloodType: String? = null,

    @SerializedName("allergies")
    val allergies: String? = null,

    @SerializedName("lastname")
    val lastname: String? = null,

    @SerializedName("nationality")
    val nationality: String? = null,

    @SerializedName("phone_emergency")
    val phoneEmergency: String? = null,

    @SerializedName("photo")
    val photo: String? = null,

    @SerializedName("area")
    val area: String? = null,

    @SerializedName("diseases")
    val diseases: String? = null
)
