package com.devpaul.estructurapublicitarias_roal.data.models.response

import com.google.gson.annotations.SerializedName

data class WorkerReportByUserResponse(
    @SerializedName("validationEPP")
    val validationData: ValidationData? = null,
    @SerializedName("validateEquipment")
    val validateEquipment: ValidationData? = null
)

data class ValidationData(
    @SerializedName("dataEntries")
    val dataEntries: DataEntries?,
    @SerializedName("additionalData")
    val additionalData: List<AdditionalData>
)

data class DataEntries(
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
    @SerializedName("key")
    val key: String,
    @SerializedName("value")
    val value: String
)
