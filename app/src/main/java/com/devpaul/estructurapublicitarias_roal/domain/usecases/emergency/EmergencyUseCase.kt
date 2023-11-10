package com.devpaul.estructurapublicitarias_roal.domain.usecases.emergency

import com.devpaul.estructurapublicitarias_roal.data.models.entity.GetWorker
import com.devpaul.estructurapublicitarias_roal.data.models.entity.ValidateImageByPhoto
import com.devpaul.estructurapublicitarias_roal.data.models.entity.Worker
import com.devpaul.estructurapublicitarias_roal.data.models.request.ValidateImageByPhotoRequest
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.interfaces.repository.WorkersRepositoryNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EmergencyUseCase(private val workersRepositoryNetwork: WorkersRepositoryNetwork) {
    suspend fun validateImageByPhoto(validateImageByPhotoRequest: ValidateImageByPhotoRequest): CustomResult<ValidateImageByPhoto> =
        withContext(Dispatchers.IO) {
            val validateImage = workersRepositoryNetwork.validateImageByPhoto(validateImageByPhotoRequest)
            when (validateImage) {
                is CustomResult.OnSuccess -> {
                    validateImage.data
                }

                else -> {
                    ValidateImageByPhoto()
                }
            }
            return@withContext validateImage
        }

    suspend fun getWorkerByPhoto(dni: String): CustomResult<GetWorker> =
        withContext(Dispatchers.IO) {
            val getWorkerByPhoto = workersRepositoryNetwork.getWorkers(dni)
            when (getWorkerByPhoto) {
                is CustomResult.OnSuccess -> {
                    getWorkerByPhoto.data
                }

                else -> {
                    ValidateImageByPhoto()
                }
            }
            return@withContext getWorkerByPhoto
        }

}