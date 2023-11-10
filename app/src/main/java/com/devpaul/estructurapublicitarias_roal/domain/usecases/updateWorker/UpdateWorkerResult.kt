package com.devpaul.estructurapublicitarias_roal.domain.usecases.updateWorker

import com.devpaul.estructurapublicitarias_roal.data.models.entity.GetWorker

sealed class UpdateWorkerResult {
    data class WorkerDataReceived(val worker: GetWorker) : UpdateWorkerResult()
    data class Error(val code: Int?, val title: String?, val subTitle: String?) : UpdateWorkerResult()
    data object ValidationError : UpdateWorkerResult()

}