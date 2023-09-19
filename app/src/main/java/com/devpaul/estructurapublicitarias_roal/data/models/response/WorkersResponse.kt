package com.devpaul.estructurapublicitarias_roal.data.models.response

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class WorkersResponse(

    @SerializedName("dni")
    val dni: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("lastname")
    val lastname: String? = null,

    @SerializedName("date_birth")
    val dateBirth: String? = null,

    @SerializedName("date_join")
    val dateJoin: String? = null,

    @SerializedName("area")
    val area: String? = null,

    @SerializedName("blood_type")
    val bloodType: String? = null,

    @SerializedName("diseases")
    val diseases: String? = null,

    @SerializedName("allergies")
    val allergies: String? = null,

    @SerializedName("phone")
    val phone: String? = null,

    @SerializedName("phone_emergency")
    val phoneEmergency: String? = null,

    @SerializedName("photo")
    val photo: String? = null,

    @SerializedName("photo_format")
    val photoFormat: String? = null
) {

    fun toJson(): String {
        return Gson().toJson(this)
    }

    override fun toString(): String {
        return "Workers(dni=$dni, name=$name, lastname=$lastname, date_birth=$dateBirth, date_join=$dateJoin, area=$area, blood_type=$bloodType, diseases=$diseases, allergies=$allergies, phone=$phone, phone_emergency=$phoneEmergency, photo=$photo, photoFormat=$photoFormat)"
    }


}
