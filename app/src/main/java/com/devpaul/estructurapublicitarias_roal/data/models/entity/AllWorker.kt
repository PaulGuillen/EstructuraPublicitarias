package com.devpaul.estructurapublicitarias_roal.data.models.entity

class AllWorker (
    val workers: List<BodyWorkerEntity>,
    val paginationInfo: PaginationInfoEntity
)

data class BodyWorkerEntity(
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

data class PaginationInfoEntity(
    val totalResult: Int
)