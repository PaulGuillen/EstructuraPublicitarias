package com.devpaul.estructurapublicitarias_roal.data.models.entity


data class WorkerReportByUser(
    var validationEPP: ValidationEPPReportEntity? = null
)

class ValidationEPPReportEntity(
    val dataEntriesEPP: DataEntriesEPPEntity? = null,
    val additionalData: AdditionalDataEntity? = null
)

data class DataEntriesEPPEntity(
    val pieEntries: List<PieEntryEntity>? = null,
    val totalValidation: String? = null
)

data class PieEntryEntity(
    val labelEntry: String? = null,
    val valueEntry: String? = null,
    val colorEntry: String? = null
)

data class AdditionalDataEntity(
    val titleReport: String? = null,
    val questionReport: String? = null
)