package com.devpaul.estructurapublicitarias_roal.data.models.response

import  com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class MainUser(
    @SerializedName("email")
    val email: String? = null,

    @SerializedName("password")
    val password: String? = null,

    @SerializedName("action")
    val action: String? = null,

    @SerializedName("code")
    val code: Int? = null,

    @SerializedName("new_password")
    val new_password: String? = null,

    @SerializedName("dni")
    val dni: String? = null,

    @SerializedName("photo")
    val photo: String? = null,

    @SerializedName("create_date_account")
    val createDateAccount: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("lastname")
    val lastname: String? = null
) {
    fun toJson(): String {
        return Gson().toJson(this)
    }

    override fun toString(): String {
        return "MainUser(email=$email, password=$password, action=$action, code=$code, new_password=$new_password)"
    }

}