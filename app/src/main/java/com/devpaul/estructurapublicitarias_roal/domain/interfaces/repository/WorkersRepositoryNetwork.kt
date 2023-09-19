package com.devpaul.estructurapublicitarias_roal.domain.interfaces.repository

import com.devpaul.estructurapublicitarias_roal.data.models.Options
import com.devpaul.estructurapublicitarias_roal.data.models.Worker
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult

interface WorkersRepositoryNetwork {
    fun getOptionList() : CustomResult<List<Options>>
    fun getWorkers(dni: String): CustomResult<Worker>

}