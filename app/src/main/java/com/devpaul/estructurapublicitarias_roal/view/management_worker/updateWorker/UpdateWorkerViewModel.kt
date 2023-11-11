package com.devpaul.estructurapublicitarias_roal.view.management_worker.updateWorker

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devpaul.estructurapublicitarias_roal.data.models.entity.GetWorker
import com.devpaul.estructurapublicitarias_roal.data.repository.WorkersRepository
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.usecases.mangementWorker.ManagementWorkerUseCase
import com.devpaul.estructurapublicitarias_roal.domain.usecases.updateWorker.UpdateWorkerResult
import com.devpaul.estructurapublicitarias_roal.domain.utils.SingletonError
import com.devpaul.estructurapublicitarias_roal.view.base.BaseViewModel
import kotlinx.coroutines.launch

class UpdateWorkerViewModel(context: Context) : BaseViewModel() {

    val textFullName = MutableLiveData("")
    val textDNI = MutableLiveData("")
    val textArea = MutableLiveData("")
    val textPhone = MutableLiveData("")
    val textPhoneEmergency = MutableLiveData("")
    val textBlood = MutableLiveData("")
    val textIllness = MutableLiveData("")
    val textAllergies = MutableLiveData("")

    var responseName = MutableLiveData("")

    private val _updateWorkerResult = MutableLiveData<UpdateWorkerResult>()

    val updateWorkerResult: LiveData<UpdateWorkerResult> = _updateWorkerResult

    fun validateWorker(document: String) {
        viewModelScope.launch {
            callServiceGetWorker(document)
        }
    }

    fun updateWorker() {
        viewModelScope.launch {
            callServiceUpdateWorker()
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

    private fun showData(worker: GetWorker?) {
        worker?.message?.run {
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

    private suspend fun callServiceUpdateWorker() {

//        val name = binding.textFirstName.text.toString()
//        val lastname = binding.textLastName.text.toString()
//        val dateBirth = binding.textdateBirth.text.toString()
//        val dateJoin = binding.textdateJoin.text.toString()
//        val area = binding.textArea.text.toString()
//        val bloodType = binding.textBlood.text.toString()
//        val diseases = binding.textIllness.text.toString()
//        val allergies = binding.textAllergies.text.toString()
//        val phone = binding.textPhone.text.toString()
//        val phoneEmergency = binding.textPhoneEmergency.text.toString()
//
//        val workerRequest = WorkerRequest(
//            dni = textDNI.value,
//            name = responseName.value,
//            lastname = lastname,
//            dateBirth = dateBirth,
//            dateJoin = dateJoin,
//            area = area,
//            bloodType = bloodType,
//            diseases = diseases,
//            allergies = allergies,
//            phone = phone,
//            phoneEmergency = phoneEmergency,
//            photo = imageBase,
//            photoFormat = imageFile?.name
//        )

//        _showLoadingDialog.value = true
//        try {
//            val workerRepository = WorkersRepository()
//            val managementWorkerUseCase = ManagementWorkerUseCase(workerRepository)
//            when (val getWorker = managementWorkerUseCase.updateWorker()) {
//                is CustomResult.OnSuccess -> {
//                    val data = getWorker.data
//                   // showData(data)
//                    _updateWorkerResult.value = UpdateWorkerResult.WorkerDataReceived(data)
//                }
//
//                is CustomResult.OnError -> {
//                    val error = getWorker.error
//                    _updateWorkerResult.value = UpdateWorkerResult.Error(
//                        error.code ?: SingletonError.code,
//                        error.title ?: SingletonError.title,
//                        error.subtitle
//                    )
//                }
//            }
//        } catch (e: Exception) {
//            _updateWorkerResult.value = UpdateWorkerResult.ValidationError
//        } finally {
//            _showLoadingDialog.value = false
//        }
//
//    }

    }
}