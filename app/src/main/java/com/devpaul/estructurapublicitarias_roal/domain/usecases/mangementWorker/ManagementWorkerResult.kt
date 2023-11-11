package com.devpaul.estructurapublicitarias_roal.domain.usecases.mangementWorker

import androidx.lifecycle.MutableLiveData
import com.devpaul.estructurapublicitarias_roal.data.models.entity.GetWorker

sealed class ManagementWorkerResult {

    data class Success(val data: GetWorker) : ManagementWorkerResult()
    data object SuccessWorkerDeleted : ManagementWorkerResult()
    data object DeleteWorker : ManagementWorkerResult()
    data class UpdateWorker(val dni: MutableLiveData<String>, val area: MutableLiveData<String>) : ManagementWorkerResult()
    data object CreateWorker : ManagementWorkerResult()
    data class Error(val code: Int?, val title: String?, val subTitle: String?) : ManagementWorkerResult()
    data object ValidationError : ManagementWorkerResult()

}