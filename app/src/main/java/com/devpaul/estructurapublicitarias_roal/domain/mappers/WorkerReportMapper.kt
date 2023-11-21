package com.devpaul.estructurapublicitarias_roal.domain.mappers

import com.devpaul.estructurapublicitarias_roal.data.models.entity.AdditionalDataEntity
import com.devpaul.estructurapublicitarias_roal.data.models.entity.DataEntriesEntity
import com.devpaul.estructurapublicitarias_roal.data.models.entity.PieDescriptionEntity
import com.devpaul.estructurapublicitarias_roal.data.models.entity.PieEntryEntity
import com.devpaul.estructurapublicitarias_roal.data.models.entity.PrincipalListWorker
import com.devpaul.estructurapublicitarias_roal.data.models.entity.ValidationReportEntity
import com.devpaul.estructurapublicitarias_roal.data.models.entity.WorkerReportByUser
import com.devpaul.estructurapublicitarias_roal.data.models.response.PrincipalListWorkerResponse
import com.devpaul.estructurapublicitarias_roal.data.models.response.ValidationData
import com.devpaul.estructurapublicitarias_roal.data.models.response.WorkerReportByUserResponse
import java.util.ArrayList

class WorkerReportMapper {
    fun map(principalListWorker: List<PrincipalListWorkerResponse>): List<PrincipalListWorker> {

        val allWorkers: MutableList<PrincipalListWorker> = ArrayList()
        for (item in principalListWorker) {
            allWorkers.add(mapOptions(item))
        }
        return allWorkers
    }

    private fun mapOptions(principalListWorker: PrincipalListWorkerResponse): PrincipalListWorker {
        return PrincipalListWorker(
            name = principalListWorker.name,
            lastname = principalListWorker.lastname,
            document = principalListWorker.document
        )
    }

    fun mapReportData(workerReportByUserResponse: WorkerReportByUserResponse): WorkerReportByUser {
        val validationReportEntity = mapValidationData(workerReportByUserResponse.validationData)
        val equipmentPresentReportEntity = mapValidationData(workerReportByUserResponse.validateEquipmentPresent)
        val equipmentMissingReportEntity = mapValidationData(workerReportByUserResponse.validateEquipmentMissing)

        return WorkerReportByUser(
            validationEPP = validationReportEntity,
            validateEquipmentPresent = equipmentPresentReportEntity,
            validateEquipmentMissing = equipmentMissingReportEntity
        )
    }

    private fun mapValidationData(validationData: ValidationData?): ValidationReportEntity? {
        return validationData?.dataEntries?.let { dataEntries ->
            val pieEntriesEntity = dataEntries.pieEntries.map { pieEntry ->
                PieEntryEntity(
                    labelEntry = pieEntry.labelEntry,
                    valueEntry = pieEntry.valueEntry,
                    colorEntry = pieEntry.colorEntry
                )
            }

            val pieDescriptionEntity = PieDescriptionEntity(
                centerText = dataEntries.pieDescription.centerText,
                topText = dataEntries.pieDescription.topText,
                bottomText = dataEntries.pieDescription.bottomText,
                totalValidations = dataEntries.pieDescription.totalValidations
            )

            val dataEntriesEntity = DataEntriesEntity(
                pieEntries = pieEntriesEntity,
                pieDescription = pieDescriptionEntity
            )

            val additionalDataEntity = validationData.additionalData.map { item ->
                AdditionalDataEntity(
                    key = item.key,
                    value = item.value
                )
            }

            ValidationReportEntity(
                dataEntries = dataEntriesEntity,
                additionalData = additionalDataEntity
            )
        }
    }

}