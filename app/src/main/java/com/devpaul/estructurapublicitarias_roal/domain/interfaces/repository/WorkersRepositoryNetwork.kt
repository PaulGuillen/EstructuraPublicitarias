package com.devpaul.estructurapublicitarias_roal.domain.interfaces.repository

import com.devpaul.estructurapublicitarias_roal.data.models.entity.ListWorker
import com.devpaul.estructurapublicitarias_roal.data.models.entity.GeneralHTTP
import com.devpaul.estructurapublicitarias_roal.data.models.entity.GetWorker
import com.devpaul.estructurapublicitarias_roal.data.models.entity.Options
import com.devpaul.estructurapublicitarias_roal.data.models.entity.ValidateImageByPhoto
import com.devpaul.estructurapublicitarias_roal.data.models.request.ValidateImageByPhotoRequest
import com.devpaul.estructurapublicitarias_roal.data.models.request.WorkerRequest
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult

interface WorkersRepositoryNetwork {
    fun createWorker(workerRequest: WorkerRequest): CustomResult<GeneralHTTP>
    fun getOptionList(): CustomResult<List<Options>>
    fun getWorkers(dni: String): CustomResult<GetWorker>
    fun validateImageByPhoto(validateImageByPhotoRequest: ValidateImageByPhotoRequest): CustomResult<ValidateImageByPhoto>
    fun deleteWorker(dni: String): CustomResult<GeneralHTTP>
    fun listWorkers(): CustomResult<ListWorker>
}