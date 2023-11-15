package com.devpaul.estructurapublicitarias_roal.domain.usecases.reportWorker

import com.devpaul.estructurapublicitarias_roal.data.models.entity.PrincipalListWorker
import com.devpaul.estructurapublicitarias_roal.data.models.entity.WorkerReportByUser

sealed class WorkerReportResult {
    data class Success(val data: List<PrincipalListWorker>) : WorkerReportResult()
    data class Error(val code: Int?, val title: String?, val subTitle: String?) : WorkerReportResult()
    data class ValidationError(val exception: Exception) : WorkerReportResult()
    data class SuccessDataReportByWorker(val data: WorkerReportByUser) : WorkerReportResult()
}