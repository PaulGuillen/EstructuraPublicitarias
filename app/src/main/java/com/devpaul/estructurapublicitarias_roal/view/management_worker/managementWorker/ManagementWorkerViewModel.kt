package com.devpaul.estructurapublicitarias_roal.view.management_worker.managementWorker

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devpaul.estructurapublicitarias_roal.data.repository.WorkersRepository
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.usecases.mangementWorker.ManagementWorkerResult
import com.devpaul.estructurapublicitarias_roal.domain.usecases.mangementWorker.ManagementWorkerUseCase
import com.devpaul.estructurapublicitarias_roal.domain.utils.SingletonError
import com.devpaul.estructurapublicitarias_roal.view.base.BaseViewModel
import kotlinx.coroutines.launch

class ManagementWorkerViewModel(context: Context) : BaseViewModel() {

    var documentNumber = MutableLiveData("")
    var textDNI = MutableLiveData("")
    private var textArea = MutableLiveData("")

    private val _managementWorkerResult = MutableLiveData<ManagementWorkerResult>()

    val managementWorkerResult: LiveData<ManagementWorkerResult> = _managementWorkerResult

    fun validateWorkerByDNI() {
        viewModelScope.launch {
            callServiceGetWorkerByDNI()
        }
    }

    fun validateDeleteWorker() {
        viewModelScope.launch {
            callServiceDeleteWorker()
        }
    }

    private suspend fun callServiceGetWorkerByDNI() {

        _showLoadingDialog.value = true
        try {
            val workerRepository = WorkersRepository()
            val managementWorkerUseCase = ManagementWorkerUseCase(workerRepository)
            when (val getWorker = managementWorkerUseCase.getWorker(documentNumber.value.toString())) {
                is CustomResult.OnSuccess -> {
                    val data = getWorker.data
                    textDNI.postValue(data.message?.dni.toString())
                    textArea.postValue(data.message?.area.toString())
                    _managementWorkerResult.value = ManagementWorkerResult.Success(data)
                }

                is CustomResult.OnError -> {
                    val error = getWorker.error
                    _managementWorkerResult.value = ManagementWorkerResult.Error(
                        error.code ?: SingletonError.code,
                        error.title ?: SingletonError.title,
                        error.subtitle
                    )
                }
            }
        } catch (e: Exception) {
            _managementWorkerResult.value = ManagementWorkerResult.ValidationError
        } finally {
            _showLoadingDialog.value = false
        }

    }

    private suspend fun callServiceDeleteWorker() {

        _showLoadingDialog.value = true
        try {
            val workerRepository = WorkersRepository()
            val managementWorkerUseCase = ManagementWorkerUseCase(workerRepository)
            when (val deleteWorker = managementWorkerUseCase.deleteWorker(documentNumber.value.toString())) {
                is CustomResult.OnSuccess -> {
                    _managementWorkerResult.value = ManagementWorkerResult.SuccessWorkerDeleted
                }

                is CustomResult.OnError -> {
                    val error = deleteWorker.error
                    _managementWorkerResult.value = ManagementWorkerResult.Error(
                        error.code ?: SingletonError.code,
                        error.title ?: SingletonError.title,
                        error.subtitle
                    )
                }
            }
        } catch (e: Exception) {
            _managementWorkerResult.value = ManagementWorkerResult.ValidationError
        } finally {
            _showLoadingDialog.value = false
        }

    }

    fun deleteWorker() {
        _managementWorkerResult.value = ManagementWorkerResult.DeleteWorker
    }

    fun updateWorker() {
        _managementWorkerResult.value = ManagementWorkerResult.UpdateWorker(textDNI,textArea)
    }

    fun createWorker() {
        _managementWorkerResult.value = ManagementWorkerResult.CreateWorker
    }

}