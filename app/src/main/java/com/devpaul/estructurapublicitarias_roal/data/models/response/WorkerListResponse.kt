package com.devpaul.estructurapublicitarias_roal.data.models.response

import com.google.gson.annotations.SerializedName

data class ListWorkersResponse(
    @SerializedName("listWorkers")
    val listWorkers: List<WorkerList>,
    @SerializedName("paginationInfo")
    val paginationInfo: PaginationInfo
)

data class WorkerList(
    @SerializedName("dni")
    val dni: String,
    @SerializedName("date_birth")
    val dateBirth: String,
    @SerializedName("date_join")
    val dateJoin: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("blood_type")
    val bloodType: String,
    @SerializedName("allergies")
    val allergies: String,
    @SerializedName("lastname")
    val lastname: String,
    @SerializedName("nationality")
    val nationality: String,
    @SerializedName("phone_emergency")
    val phoneEmergency: String,
    @SerializedName("photo")
    val photo: String,
    @SerializedName("diseases")
    val diseases: String,
    @SerializedName("area")
    val area: String
)

data class PaginationInfo(
    @SerializedName("totalResult") val totalResult: Int
)