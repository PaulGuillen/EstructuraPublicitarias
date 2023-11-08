package com.devpaul.estructurapublicitarias_roal.domain.usecases.mangementWorker

import com.devpaul.estructurapublicitarias_roal.data.models.entity.Worker

sealed class ManagementWorkerResult {

    data class Success(val data: Worker) : ManagementWorkerResult()
    data object DeleteWorker : ManagementWorkerResult()
    data class UpdateWorker(val dni: String) : ManagementWorkerResult()
    data object CreateWorker : ManagementWorkerResult()
    data class Error(val code: Int?, val title: String?, val subTitle: String?) : ManagementWorkerResult()
    data object ValidationError : ManagementWorkerResult()

}