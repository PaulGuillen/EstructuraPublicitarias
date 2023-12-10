package com.devpaul.estructurapublicitarias_roal.data.models.entity
data class ListWorker(
    var workers: List<BodyWorker>? = null,
    var paginationInfo: PaginationInfoResponse? = null
)

data class BodyWorker(
    val dni: String,
    val dateBirth: String,
    val dateJoin: String,
    val name: String,
    val gender: String,
    val phone: String,
    val bloodType: String,
    val allergies: String,
    val lastname: String,
    val nationality: String,
    val phoneEmergency: String,
    val photo: String,
    val diseases: String,
    val area: String
)

data class PaginationInfoResponse(
    val totalResult: Int
)