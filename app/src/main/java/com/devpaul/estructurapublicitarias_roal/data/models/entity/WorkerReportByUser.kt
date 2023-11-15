package com.devpaul.estructurapublicitarias_roal.data.models.entity

data class WorkerReportByUser(
    val validationEPP: ValidationEPPReportEntity? = null
)

data class ValidationEPPReportEntity(
    val dataEntriesEPP: DataEntriesEPPEntity,
    val additionalData: List<AdditionalDataEntity>
)

data class DataEntriesEPPEntity(
    val pieEntries: List<PieEntryEntity>,
    val pieDescription: PieDescriptionEntity
)

data class PieEntryEntity(
    val labelEntry: String,
    val valueEntry: String,
    val colorEntry: String
)

data class PieDescriptionEntity(
    val centerText: String,
    val topText: String,
    val bottomText: String,
    val totalValidations: String
)

data class AdditionalDataEntity(
    val key: String,
    val value: String
)
