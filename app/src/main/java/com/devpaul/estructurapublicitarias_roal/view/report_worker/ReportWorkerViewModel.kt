package com.devpaul.estructurapublicitarias_roal.view.report_worker

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devpaul.estructurapublicitarias_roal.data.repository.WorkersReportRepository
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.usecases.reportWorker.WorkerReportResult
import com.devpaul.estructurapublicitarias_roal.domain.usecases.reportWorker.WorkerReportUseCase
import com.devpaul.estructurapublicitarias_roal.domain.utils.SingletonError
import com.devpaul.estructurapublicitarias_roal.view.base.BaseViewModel
import kotlinx.coroutines.launch

class ReportWorkerViewModel(context: Context) : BaseViewModel() {

    private val _reportWorkerResult = MutableLiveData<WorkerReportResult>()

    val reportWorkerResult: LiveData<WorkerReportResult> = _reportWorkerResult

    fun validateAllWorkers() {
        viewModelScope.launch {
            callServiceAllWorkers()
        }
    }

    private suspend fun callServiceAllWorkers() {

        _showLoadingDialog.value = true
        try {
            val workerReportRepository = WorkersReportRepository()
            val managementWorkerReportUseCase = WorkerReportUseCase(workerReportRepository)
            when (val getWorker = managementWorkerReportUseCase.allWorkers()) {
                is CustomResult.OnSuccess -> {
                    val data = getWorker.data
                    _reportWorkerResult.value = WorkerReportResult.Success(data)
                }

                is CustomResult.OnError -> {
                    val error = getWorker.error
                    _reportWorkerResult.value = WorkerReportResult.Error(
                        error.code ?: SingletonError.code,
                        error.title ?: SingletonError.title,
                        error.subtitle ?: SingletonError.subTitle
                    )
                }
            }
        } catch (e: Exception) {
            _reportWorkerResult.value = WorkerReportResult.ValidationError(e)
        } finally {
            _showLoadingDialog.value = false
        }

    }

}