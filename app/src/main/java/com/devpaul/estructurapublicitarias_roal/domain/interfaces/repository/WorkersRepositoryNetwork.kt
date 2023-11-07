package com.devpaul.estructurapublicitarias_roal.domain.interfaces.repository

import com.devpaul.estructurapublicitarias_roal.data.models.entity.Options
import com.devpaul.estructurapublicitarias_roal.data.models.entity.ValidateImageByPhoto
import com.devpaul.estructurapublicitarias_roal.data.models.entity.Worker
import com.devpaul.estructurapublicitarias_roal.data.models.request.ValidateImageByPhotoRequest
import com.devpaul.estructurapublicitarias_roal.data.models.response.ValidateImageByPhotoResponse
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult

interface WorkersRepositoryNetwork {
    fun getOptionList() : CustomResult<List<Options>>
    fun getWorkers(dni: String): CustomResult<Worker>
    fun validateImageByPhoto(validateImageByPhotoRequest: ValidateImageByPhotoRequest) : CustomResult<ValidateImageByPhoto>

}