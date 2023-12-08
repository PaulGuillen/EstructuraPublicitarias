package com.devpaul.estructurapublicitarias_roal.data.models.response

import com.google.gson.annotations.SerializedName

class AllWorkerResponse (
    @SerializedName("workers")
    val workers: List<BodyWorkerResponse>,
    @SerializedName("paginationInfo")
    val paginationInfo: PaginationInfoResponse
)

data class BodyWorkerResponse(
    @SerializedName("dni")
    val dni: String,
    @SerializedName("date_birth")
    val date_birth: String,
    @SerializedName("date_join")
    val date_join: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("blood_type")
    val blood_type: String,
    @SerializedName("allergies")
    val allergies: String,
    @SerializedName("lastname")
    val lastname: String,
    @SerializedName("nationality")
    val nationality: String,
    @SerializedName("phone_emergency")
    val phone_emergency: String,
    @SerializedName("photo")
    val photo: String,
    @SerializedName("diseases")
    val diseases: String,
    @SerializedName("area")
    val area: String
)

data class PaginationInfoResponse(
    @SerializedName("totalResult")
    val totalResult: Int
)