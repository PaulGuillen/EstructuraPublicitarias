package com.devpaul.estructurapublicitarias_roal.domain.usecases.mangementWorker

import com.devpaul.estructurapublicitarias_roal.data.models.entity.GeneralHTTP
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


    suspend fun deleteWorker(dni: String): CustomResult<GeneralHTTP> =
        withContext(Dispatchers.IO) {
            val deleteWorker = workersRepositoryNetwork.deleteWorker(dni)
            when (deleteWorker) {
                is CustomResult.OnSuccess -> {
                    deleteWorker.data
                }

                else -> {
                    GeneralHTTP()
                }
            }
            return@withContext deleteWorker
        }

//    suspend fun updateWorker(worker: Worker): CustomResult<Worker> =
//        withContext(Dispatchers.IO) {
//            val updateWorker = workersRepositoryNetwork.updateWorker(worker)
//            when (updateWorker) {
//                is CustomResult.OnSuccess -> {
//                    updateWorker.data
//                }
//
//                else -> {
//                    Worker()
//                }
//            }
//            return@withContext updateWorker
//        }

}