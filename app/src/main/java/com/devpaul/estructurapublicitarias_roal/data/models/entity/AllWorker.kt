package com.devpaul.estructurapublicitarias_roal.data.models.entity

import com.devpaul.estructurapublicitarias_roal.data.models.response.PaginationInfoResponse

data class AllWorker(
    var workers: List<BodyWorker>? = null,
    var paginationInfo: PaginationInfoResponse? = null
)

data class BodyWorker(
    val dni: String,
    val date_birth: String,
    val date_join: String,
    val name: String,
    val gender: String,
    val phone: String,
    val blood_type: String,
    val allergies: String,
    val lastname: String,
    val nationality: String,
    val phone_emergency: String,
    val photo: String,
    val diseases: String,
    val area: String
)

data class PaginationInfoResponse(
    val totalResult: Int
)