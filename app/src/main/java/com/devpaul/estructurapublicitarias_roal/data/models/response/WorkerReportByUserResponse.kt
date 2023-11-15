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
    val additionalData: List<AdditionalData>
)

data class DataEntriesEPP(
    @SerializedName("pieEntries")
    val pieEntries: List<PieEntry>,
    @SerializedName("pieDescription")
    val pieDescription: PieDescription
)

data class PieDescription(
    @SerializedName("centerText")
    val centerText: String,
    @SerializedName("topText")
    val topText: String,
    @SerializedName("bottomText")
    val bottomText: String,
    @SerializedName("totalValidations")
    val totalValidations: String
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
    @SerializedName("key") val key: String,
    @SerializedName("value") val value: String
)
