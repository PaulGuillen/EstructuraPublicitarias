package com.devpaul.estructurapublicitarias_roal.domain.usecases

import android.content.Context
import com.devpaul.estructurapublicitarias_roal.data.models.entity.GetWorker
import com.devpaul.estructurapublicitarias_roal.data.models.entity.Options
import com.devpaul.estructurapublicitarias_roal.data.models.entity.Worker
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.interfaces.repository.WorkersRepositoryNetwork
import com.devpaul.estructurapublicitarias_roal.domain.utils.SharedPref

class WorkerUseCase(
    var context: Context,
    private val workersRepositoryNetwork: WorkersRepositoryNetwork,
) {
    private val prefs = SharedPref(context)
    fun getWorkers(dni: String): CustomResult<GetWorker> {
        val worker = workersRepositoryNetwork.getWorkers(dni)
        when (worker) {
            is CustomResult.OnSuccess -> {
                worker.data
            }

            else -> {
                GetWorker()
            }
        }
        return worker
    }

    fun getOptions(): CustomResult<List<Options>> {
        val options = workersRepositoryNetwork.getOptionList()
        when (options) {
            is CustomResult.OnSuccess -> {
                saveOptionsCategories(options.data)
            }

            else -> {
                saveOptionsCategories(emptyList())
            }
        }
        return options
    }

    private fun saveOptionsCategories(options: List<Options>) {
        if (!options.firstOrNull()?.optionID.isNullOrEmpty()) {
            prefs.saveJsonObject("optionsCategory", options)
        }
    }


}