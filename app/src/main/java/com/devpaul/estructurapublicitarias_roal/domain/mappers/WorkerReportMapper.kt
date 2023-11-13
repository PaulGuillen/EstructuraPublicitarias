package com.devpaul.estructurapublicitarias_roal.domain.mappers

import com.devpaul.estructurapublicitarias_roal.data.models.entity.PrincipalListWorker
import com.devpaul.estructurapublicitarias_roal.data.models.response.PrincipalListWorkerResponse

class WorkerReportMapper {

    fun map(principalListWorker: PrincipalListWorkerResponse): PrincipalListWorker {
        return PrincipalListWorker(
            name = principalListWorker.name,
            lastname = principalListWorker.lastname,
            document = principalListWorker.document
        )
    }
}