package com.devpaul.estructurapublicitarias_roal.domain.usecases.emergency


import com.devpaul.estructurapublicitarias_roal.data.models.entity.GetWorker

sealed class EmergencyResult {
    data class SuccessWorker(val data: GetWorker) : EmergencyResult()
    data object Error : EmergencyResult()
    data object ValidationError : EmergencyResult()
}

