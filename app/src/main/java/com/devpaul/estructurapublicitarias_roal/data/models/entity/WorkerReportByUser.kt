package com.devpaul.estructurapublicitarias_roal.data.models.entity

data class WorkerReportByUser(
    val validationEPP: ValidationReportEntity? = null,
    val validateEquipment: ValidationReportEntity? = null
)

data class ValidationReportEntity(
    val dataEntries: DataEntriesEntity,
    val additionalData: List<AdditionalDataEntity>?
)

data class DataEntriesEntity(
    val pieEntries: List<PieEntryEntity>?,
    val pieDescription: PieDescriptionEntity
)

data class PieEntryEntity(
    val labelEntry: String,
    val valueEntry: String,
    val colorEntry: String
)

data class PieDescriptionEntity(
    val centerText: String? = null,
    val topText: String? = null,
    val bottomText: String? = null,
    val totalValidations: String? = null
)

data class AdditionalDataEntity(
    val key: String? = null,
    val value: String? = null
)
