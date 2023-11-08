package com.devpaul.estructurapublicitarias_roal.domain.usecases.mangementWorker

import com.devpaul.estructurapublicitarias_roal.data.models.entity.ValidateImageByPhoto
import com.devpaul.estructurapublicitarias_roal.data.models.entity.Worker
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.interfaces.repository.WorkersRepositoryNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ManagementWorkerUseCase(private val workersRepositoryNetwork: WorkersRepositoryNetwork) {
    suspend fun getWorker(dni: String): CustomResult<Worker> =
        withContext(Dispatchers.IO) {
            val getWorker = workersRepositoryNetwork.getWorkers(dni)
            when (getWorker) {
                is CustomResult.OnSuccess -> {
                    getWorker.data
                }

                else -> {
                    ValidateImageByPhoto()
                }
            }
            return@withContext getWorker
        }
}