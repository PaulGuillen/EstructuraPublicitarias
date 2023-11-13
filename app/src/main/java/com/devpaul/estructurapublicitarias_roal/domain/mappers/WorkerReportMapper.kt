package com.devpaul.estructurapublicitarias_roal.domain.mappers

import com.devpaul.estructurapublicitarias_roal.data.models.entity.PrincipalListWorker
import com.devpaul.estructurapublicitarias_roal.data.models.response.PrincipalListWorkerResponse
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
}