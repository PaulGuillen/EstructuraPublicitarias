package com.devpaul.estructurapublicitarias_roal.data.models.response

import com.google.gson.annotations.SerializedName

data class PrincipalListWorkerResponse(
    @SerializedName("name")
    val name: String? = null,

    @SerializedName("lastname")
    val lastname: String? = null,

    @SerializedName("document")
    val document: Int? = null
) {
    override fun toString(): String {
        return "PrincipalListWorkerResponse(name=$name, lastname=$lastname, document=$document)"
    }
}