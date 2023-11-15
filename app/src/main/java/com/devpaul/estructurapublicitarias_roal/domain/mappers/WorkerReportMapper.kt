package com.devpaul.estructurapublicitarias_roal.domain.mappers

import com.devpaul.estructurapublicitarias_roal.data.models.entity.AdditionalDataEntity
import com.devpaul.estructurapublicitarias_roal.data.models.entity.AllEquipment
import com.devpaul.estructurapublicitarias_roal.data.models.entity.DataEntriesEPPEntity
import com.devpaul.estructurapublicitarias_roal.data.models.entity.PieDescriptionEntity
import com.devpaul.estructurapublicitarias_roal.data.models.entity.PieEntryEntity
import com.devpaul.estructurapublicitarias_roal.data.models.entity.PrincipalListWorker
import com.devpaul.estructurapublicitarias_roal.data.models.entity.ValidationEPPReportEntity
import com.devpaul.estructurapublicitarias_roal.data.models.entity.WorkerReportByUser
import com.devpaul.estructurapublicitarias_roal.data.models.response.AdditionalData
import com.devpaul.estructurapublicitarias_roal.data.models.response.DataEntriesEPP
import com.devpaul.estructurapublicitarias_roal.data.models.response.PieDescription
import com.devpaul.estructurapublicitarias_roal.data.models.response.PieEntry
import com.devpaul.estructurapublicitarias_roal.data.models.response.PrincipalListWorkerResponse
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
        val pieEntries = workerReportByUserResponse.validationEPP.dataEntriesEPP
        val additionalData = workerReportByUserResponse.validationEPP.additionalData
        val pieDescription = workerReportByUserResponse.validationEPP.dataEntriesEPP.pieDescription

        val pieEntriesEntity = pieEntries.pieEntries.map { pieEntry ->
            PieEntryEntity(
                labelEntry = pieEntry.labelEntry,
                valueEntry = pieEntry.valueEntry,
                colorEntry = pieEntry.colorEntry
            )
        }

        val pieDescriptionEntity = PieDescriptionEntity(
            centerText = pieDescription.centerText,
            topText = pieDescription.topText,
            bottomText = pieDescription.bottomText,
            totalValidations = pieDescription.totalValidations
        )


        val dataEntriesEntity = DataEntriesEPPEntity(
            pieEntries = pieEntriesEntity,
            pieDescription = pieDescriptionEntity
        )

        val additionalDataEntity = additionalData.map { item ->
            AdditionalDataEntity(
                key = item.key,
                value = item.value
            )
        }

        val validationEPPReportEntity = ValidationEPPReportEntity(
            dataEntriesEPP = dataEntriesEntity,
            additionalData = additionalDataEntity
        )

        return WorkerReportByUser(validationEPPReportEntity)
    }

}