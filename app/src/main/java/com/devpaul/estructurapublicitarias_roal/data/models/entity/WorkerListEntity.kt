package com.devpaul.estructurapublicitarias_roal.data.models.entity

data class WorkerListEntity(
    val listWorkers: List<WorkerList>,
    val paginationInfo: PaginationInfo
)

data class WorkerList(
    val dni: String,
    val dateBirth: String,
    val dateJoin: String,
    val name: String,
    val gender: String,
    val phone: String,
    val allergies: String,
    val lastname: String,
    val nationality: String,
    val phoneEmergency: String,
    val photo: String,
    val diseases: String,
    val area: String
)

data class PaginationInfo(
    val totalResult: Int
)