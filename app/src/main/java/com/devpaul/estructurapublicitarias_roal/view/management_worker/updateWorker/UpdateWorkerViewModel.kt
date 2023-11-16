package com.devpaul.estructurapublicitarias_roal.view.management_worker.updateWorker

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devpaul.estructurapublicitarias_roal.data.models.entity.GetWorker
import com.devpaul.estructurapublicitarias_roal.data.models.request.WorkerRequest
import com.devpaul.estructurapublicitarias_roal.data.repository.WorkersRepository
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.usecases.mangementWorker.ManagementWorkerUseCase
import com.devpaul.estructurapublicitarias_roal.domain.usecases.updateWorker.UpdateWorkerResult
import com.devpaul.estructurapublicitarias_roal.domain.utils.SingletonError
import com.devpaul.estructurapublicitarias_roal.domain.utils.isValidFormUpdateWorker
import com.devpaul.estructurapublicitarias_roal.view.base.BaseViewModel
import kotlinx.coroutines.launch
import java.io.File

class UpdateWorkerViewModel(context: Context) : BaseViewModel() {

    val textFullName = MutableLiveData("")
    val textDNI = MutableLiveData("")
    val textPhone = MutableLiveData("")
    val textPhoneEmergency = MutableLiveData("")
    val textBlood = MutableLiveData("")
    val textIllness = MutableLiveData("")
    val textAllergies = MutableLiveData("")

    private var workerData: GetWorker? = null

    private val _updateWorkerResult = MutableLiveData<UpdateWorkerResult>()

    val updateWorkerResult: LiveData<UpdateWorkerResult> = _updateWorkerResult

    fun validateWorker(document: String) {
        viewModelScope.launch {
            callServiceGetWorker(document)
        }
    }

    fun updateWorkerNoImage(areaSelected: String?) {
        viewModelScope.launch {
            callServiceUpdateWorkerNoImage(areaSelected)
        }
    }

    fun updateWorkerImage(imageFile: File?, imageInBase64: String?, areaSelected: String?) {
        viewModelScope.launch {
            callServiceUpdateWorkerImage(imageFile, imageInBase64, areaSelected)
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
                    workerData = data
                }

                is CustomResult.OnError -> {
                    val error = getWorker.error
                    _updateWorkerResult.value = UpdateWorkerResult.Error(
                        error.code ?: SingletonError.code,
                        error.title ?: SingletonError.title,
                        error.subtitle ?: SingletonError.subTitle
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
            textPhone.value = phone
            textPhoneEmergency.value = phoneEmergency
            textBlood.value = bloodType
            textIllness.value = diseases
            textAllergies.value = allergies
        }
    }

    private suspend fun callServiceUpdateWorkerNoImage(areaSelected: String?) {

        val worker = workerData?.message ?: return

        val dni = worker.dni
        val name = worker.name
        val lastname = worker.lastname
        val gender = worker.gender
        val nationality = worker.nationality
        val dateBirth = worker.dateBirth
        val dateJoin = worker.dateJoin
        val bloodType = textBlood.value
        val diseases = textIllness.value
        val allergies = textAllergies.value
        val phone = textPhone.value
        val phoneEmergency = textPhoneEmergency.value


        if (!isValidFormUpdateWorker(
                area = areaSelected.toString(),
                phone = phone.toString(),
                phoneEmergency = phoneEmergency.toString()
            )
        ) {
            _updateWorkerResult.value = UpdateWorkerResult.ValidationError
            _showLoadingDialog.value = false
            return
        }

        val workerUpdateData = WorkerRequest(
            dni = dni,
            name = name,
            lastname = lastname,
            gender = gender,
            nationality = nationality,
            dateBirth = dateBirth,
            admissionDate = dateJoin,
            area = areaSelected,
            bloodType = bloodType,
            diseases = diseases,
            allergies = allergies,
            phone = phone,
            phoneEmergency = phoneEmergency,
        )

        _showLoadingDialog.value = true
        try {
            val workerRepository = WorkersRepository()
            val managementWorkerUseCase = ManagementWorkerUseCase(workerRepository)
            when (val updateWorker = managementWorkerUseCase.updateWorker(workerUpdateData)) {
                is CustomResult.OnSuccess -> {
                    _updateWorkerResult.value = UpdateWorkerResult.UpdateWorkerSuccess
                }

                is CustomResult.OnError -> {
                    val error = updateWorker.error
                    _updateWorkerResult.value = UpdateWorkerResult.Error(
                        error.code ?: SingletonError.code,
                        error.title ?: SingletonError.title,
                        error.subtitle ?: SingletonError.subTitle
                    )
                }
            }
        } catch (e: Exception) {
            _updateWorkerResult.value = UpdateWorkerResult.ValidationError
        } finally {
            _showLoadingDialog.value = false
        }
    }

    private suspend fun callServiceUpdateWorkerImage(imageFile: File?, imageInBase64: String?, areaSelected: String?) {

        val newFileName = String.format("%s.jpg", imageFile?.nameWithoutExtension)

        val worker = workerData?.message ?: return
        val dni = worker.dni
        val name = worker.name
        val lastname = worker.lastname
        val gender = worker.gender
        val nationality = worker.nationality
        val dateBirth = worker.dateBirth
        val dateJoin = worker.dateJoin
        val bloodType = textBlood.value
        val diseases = textIllness.value
        val allergies = textAllergies.value
        val phone = textPhone.value
        val phoneEmergency = textPhoneEmergency.value


        val workerUpdateData = WorkerRequest(
            dni = dni,
            name = name,
            lastname = lastname,
            gender = gender,
            nationality = nationality,
            dateBirth = dateBirth,
            admissionDate = dateJoin,
            area = areaSelected,
            bloodType = bloodType,
            diseases = diseases,
            allergies = allergies,
            phone = phone,
            phoneEmergency = phoneEmergency,
            photo = imageInBase64,
            photoFormat = newFileName
        )

        _showLoadingDialog.value = true

        try {
            val workerRepository = WorkersRepository()
            val managementWorkerUseCase = ManagementWorkerUseCase(workerRepository)
            when (val updateWorker = managementWorkerUseCase.updateWorker(workerUpdateData)) {
                is CustomResult.OnSuccess -> {
                    _updateWorkerResult.value = UpdateWorkerResult.UpdateWorkerSuccess
                }

                is CustomResult.OnError -> {
                    val error = updateWorker.error
                    _updateWorkerResult.value = UpdateWorkerResult.Error(
                        error.code ?: SingletonError.code,
                        error.title ?: SingletonError.title,
                        error.subtitle ?: SingletonError.subTitle
                    )
                }
            }
        } catch (e: Exception) {
            _updateWorkerResult.value = UpdateWorkerResult.ValidationError
        } finally {
            _showLoadingDialog.value = false
        }
    }

    fun updateWorker() {
        _updateWorkerResult.value = UpdateWorkerResult.UpdateWorker
    }
}