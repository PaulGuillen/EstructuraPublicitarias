package com.devpaul.estructurapublicitarias_roal.domain.interfaces.repository

import com.devpaul.estructurapublicitarias_roal.data.models.entity.PrincipalListWorker
import com.devpaul.estructurapublicitarias_roal.data.models.entity.WorkerReportByUser
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult

interface WorkersReportRepositoryNetwork {
    fun reportAllWorkers(): CustomResult<List<PrincipalListWorker>>
    fun reportByWorker(document: String): CustomResult<WorkerReportByUser>
}