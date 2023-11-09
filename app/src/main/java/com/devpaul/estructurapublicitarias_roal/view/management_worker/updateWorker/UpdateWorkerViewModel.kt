package com.devpaul.estructurapublicitarias_roal.view.management_worker.updateWorker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devpaul.estructurapublicitarias_roal.data.models.entity.Worker
import com.devpaul.estructurapublicitarias_roal.data.repository.WorkersRepository
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.usecases.mangementWorker.ManagementWorkerUseCase
import com.devpaul.estructurapublicitarias_roal.domain.usecases.updateWorker.UpdateWorkerResult
import com.devpaul.estructurapublicitarias_roal.domain.utils.SingletonError
import com.devpaul.estructurapublicitarias_roal.view.base.BaseViewModel
import kotlinx.coroutines.launch

class UpdateWorkerViewModel : BaseViewModel() {

    val textFullName = MutableLiveData("")
    val textDNI = MutableLiveData("")
    val textArea = MutableLiveData("")
    val textPhone = MutableLiveData("")
    val textPhoneEmergency = MutableLiveData("")
    val textBlood = MutableLiveData("")
    val textIllness = MutableLiveData("")
    val textAllergies = MutableLiveData("")

    private val _updateWorkerResult = MutableLiveData<UpdateWorkerResult>()

    val updateWorkerResult: LiveData<UpdateWorkerResult> = _updateWorkerResult

    fun validateWorker(document: String) {
        viewModelScope.launch {
            callServiceGetWorker(document)
        }
    }

    private suspend fun callServiceGetWorker(document: String) {

        _showLoadingDialog.value = true
        try {
            val workerRepository = WorkersRepository()
            val managementWorkerUseCase = ManagementWorkerUseCase(workerRepository)
            when (val getWorker = managementWorkerUseCase.getWorker(document)) {
                is CustomResult.OnSuccess -> {
                    val data = getWorker.data
                    showData(data)
                    _updateWorkerResult.value = UpdateWorkerResult.WorkerDataReceived(data)
                }

                is CustomResult.OnError -> {
                    val error = getWorker.error
                    _updateWorkerResult.value = UpdateWorkerResult.Error(
                        error.code ?: SingletonError.code,
                        error.title ?: SingletonError.title,
                        error.subtitle
                    )
                }
            }
        } catch (e: Exception) {
            _updateWorkerResult.value = UpdateWorkerResult.ValidationError
        } finally {
            _showLoadingDialog.value = false
        }

    }

    private fun showData(worker: Worker?) {
        worker?.run {
            val responseName = name
            val responseLastName = lastname
            val responseFullName = "$responseName $responseLastName"

            textDNI.value = dni
            textFullName.value = responseFullName
            textArea.value = area
            textPhone.value = phone
            textPhoneEmergency.value = phoneEmergency
            textBlood.value = bloodType
            textIllness.value = diseases
            textAllergies.value = allergies
        }
    }

}