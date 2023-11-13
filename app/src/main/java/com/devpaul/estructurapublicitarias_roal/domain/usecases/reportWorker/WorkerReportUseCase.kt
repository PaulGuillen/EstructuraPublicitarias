package com.devpaul.estructurapublicitarias_roal.domain.usecases.reportWorker

import com.devpaul.estructurapublicitarias_roal.data.models.entity.PrincipalListWorker
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.interfaces.repository.WorkersReportRepositoryNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WorkerReportUseCase(private val workersReportRepositoryNetwork: WorkersReportRepositoryNetwork) {
    suspend fun allWorkers(dni: String): CustomResult<PrincipalListWorker> =
        withContext(Dispatchers.IO) {
            val allWorkers = workersReportRepositoryNetwork.allWorkers()
            when (allWorkers) {
                is CustomResult.OnSuccess -> {
                    allWorkers.data
                }

                else -> {
                    PrincipalListWorker()
                }
            }
            return@withContext allWorkers
        }

}