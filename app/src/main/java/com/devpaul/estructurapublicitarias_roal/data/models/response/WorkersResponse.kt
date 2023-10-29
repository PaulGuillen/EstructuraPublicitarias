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

    @SerializedName("admission_date")
    val admissionDate: String? = null,

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
    val photoFormat: String? = null,

    @SerializedName("gender")
    val gender: String? = null,

    @SerializedName("nationality")
    val nationality: String? = null
) {

    fun toJson(): String {
        return Gson().toJson(this)
    }

    override fun toString(): String {
        return "WorkersResponse(dni=$dni, name=$name, lastname=$lastname, dateBirth=$dateBirth, dateJoin=$dateJoin, admissionDate=$admissionDate, area=$area, bloodType=$bloodType, diseases=$diseases, allergies=$allergies, phone=$phone, phoneEmergency=$phoneEmergency, photo=$photo, photoFormat=$photoFormat, gender=$gender, nationality=$nationality)"
    }

}
