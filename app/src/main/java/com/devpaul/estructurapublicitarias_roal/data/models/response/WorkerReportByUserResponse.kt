package com.devpaul.estructurapublicitarias_roal.data.models.response

import com.google.gson.annotations.SerializedName

data class WorkerReportByUserResponse(
    @SerializedName("validationEPP")
    val validationEPP: ValidationEPP
)

data class ValidationEPP(
    @SerializedName("dataEntriesEPP")
    val dataEntriesEPP: DataEntriesEPP,
    @SerializedName("additionalData")
    val additionalData: AdditionalData
)

data class DataEntriesEPP(
    @SerializedName("pieEntries")
    val pieEntries: List<PieEntry>,
    @SerializedName("totalValidation")
    val totalValidation: String
)

data class PieEntry(
    @SerializedName("labelEntry")
    val labelEntry: String,
    @SerializedName("valueEntry")
    val valueEntry: String,
    @SerializedName("colorEntry")
    val colorEntry: String
)

data class AdditionalData(
    @SerializedName("titleReport")
    val titleReport: String,
    @SerializedName("questionReport")
    val questionReport: String
)