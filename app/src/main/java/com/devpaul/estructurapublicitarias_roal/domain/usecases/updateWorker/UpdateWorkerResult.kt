package com.devpaul.estructurapublicitarias_roal.domain.usecases.updateWorker

import com.devpaul.estructurapublicitarias_roal.data.models.entity.Worker

sealed class UpdateWorkerResult {
    data class WorkerDataReceived(val worker: Worker) : UpdateWorkerResult()
    data class Error(val code: Int?, val title: String?, val subTitle: String?) : UpdateWorkerResult()
    data object ValidationError : UpdateWorkerResult()

}