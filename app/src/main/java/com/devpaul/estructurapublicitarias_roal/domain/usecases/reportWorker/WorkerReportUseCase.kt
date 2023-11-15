package com.devpaul.estructurapublicitarias_roal.domain.usecases.reportWorker

import com.devpaul.estructurapublicitarias_roal.data.models.entity.PrincipalListWorker
import com.devpaul.estructurapublicitarias_roal.data.models.entity.WorkerReportByUser
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.interfaces.repository.WorkersReportRepositoryNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WorkerReportUseCase(private val workersReportRepositoryNetwork: WorkersReportRepositoryNetwork) {
    suspend fun allWorkers(): CustomResult<List<PrincipalListWorker>> =
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

    suspend fun reportByWorker(document: String): CustomResult<WorkerReportByUser> =
        withContext(Dispatchers.IO) {

            val reportWorker = workersReportRepositoryNetwork.reportByWorker(document)

            when (reportWorker) {
                is CustomResult.OnSuccess -> {
                    reportWorker.data
                }

                else -> {
                    WorkerReportByUser()
                }
            }
            return@withContext reportWorker
        }

}