package com.devpaul.estructurapublicitarias_roal.domain.interfaces.repository

import com.devpaul.estructurapublicitarias_roal.data.models.entity.PrincipalListWorker
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult

interface WorkersReportRepositoryNetwork {
    fun allWorkers(): CustomResult<PrincipalListWorker>
}