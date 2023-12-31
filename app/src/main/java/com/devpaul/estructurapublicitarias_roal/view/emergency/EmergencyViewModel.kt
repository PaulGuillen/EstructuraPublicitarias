package com.devpaul.estructurapublicitarias_roal.view.emergency

import android.content.Context
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devpaul.estructurapublicitarias_roal.data.models.entity.GetWorker
import com.devpaul.estructurapublicitarias_roal.data.models.request.ValidateImageByPhotoRequest
import com.devpaul.estructurapublicitarias_roal.data.repository.WorkersRepository
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.usecases.emergency.EmergencyResult
import com.devpaul.estructurapublicitarias_roal.domain.usecases.emergency.EmergencyUseCase
import com.devpaul.estructurapublicitarias_roal.domain.utils.*
import com.devpaul.estructurapublicitarias_roal.view.base.BaseViewModel
import kotlinx.coroutines.launch
import java.io.File

class EmergencyViewModel(context: Context) : BaseViewModel() {

    private val prefs = SharedPref(context)

    val searchBoxText = MutableLiveData<String>()
    val textArea = MutableLiveData("")
    val textDNI = MutableLiveData("")
    val textFullName = MutableLiveData("")
    val principalPhone = MutableLiveData("")
    val secondaryPhone = MutableLiveData("")
    val textBornDate = MutableLiveData("")
    val textJoinDate = MutableLiveData("")
    val textAllergies = MutableLiveData("")
    val textDiseases = MutableLiveData("")
    val textBloodType = MutableLiveData("")
    val textNationality = MutableLiveData("")

    private val _emergencyResult = MutableLiveData<EmergencyResult>()

    val emergencyResult: LiveData<EmergencyResult> = _emergencyResult

    fun validateImageByPhoto(imageFile: File?, imageInBase64: String?) {
        viewModelScope.launch {
            callServiceValidateWorkerByPhoto(imageFile, imageInBase64)
        }
    }

    fun validateWorkerByDNI(dni: String) {
        viewModelScope.launch {
            getWorkerByDNI(dni)
        }
    }

    private suspend fun callServiceValidateWorkerByPhoto(imageFile: File?, imageInBase64: String?) {

        val newFileName = String.format("%s.jpg", imageFile?.nameWithoutExtension)

        val validateImageByPhotoRequest = ValidateImageByPhotoRequest(
            photo = imageInBase64,
            photoFormat = newFileName
        )
        _showLoadingDialog.value = true

        try {
            val workerRepository = WorkersRepository()
            val emergencyUseCase = EmergencyUseCase(workerRepository)
            when (val validateImage = emergencyUseCase.validateImageByPhoto(validateImageByPhotoRequest)) {
                is CustomResult.OnSuccess -> {
                    val data = validateImage.data
                    val dni = data.dni
                    getWorkerByDNIResultPhoto(dni.toString())
                }

                is CustomResult.OnError -> {
                    _emergencyResult.value = EmergencyResult.Error
                }
            }
        } catch (e: Exception) {
            _emergencyResult.value = EmergencyResult.ValidationError
        } finally {
            _showLoadingDialog.value = false
        }
    }

    private suspend fun getWorkerByDNIResultPhoto(dni: String) {
        try {
            val workerRepository = WorkersRepository()
            val emergencyUseCase = EmergencyUseCase(workerRepository)
            when (val getWorkerByPhoto =  emergencyUseCase.getWorkerByPhoto(dni) ) {
                is CustomResult.OnSuccess -> {
                    val data = getWorkerByPhoto.data
                    showData(data)
                    _emergencyResult.value = EmergencyResult.SuccessWorker(data)
                }

                is CustomResult.OnError -> {
                    _emergencyResult.value = EmergencyResult.Error
                }
            }
        } catch (e: Exception) {
            _emergencyResult.value = EmergencyResult.ValidationError
        }
    }

    private suspend fun getWorkerByDNI(dni: String) {

        _showLoadingDialog.value = true

        try {
            val workerRepository = WorkersRepository()
            val emergencyUseCase = EmergencyUseCase(workerRepository)
            when (val getWorkerByPhoto =  emergencyUseCase.getWorkerByPhoto(dni) ) {
                is CustomResult.OnSuccess -> {
                    val data = getWorkerByPhoto.data
                    showData(data)
                    _emergencyResult.value = EmergencyResult.SuccessWorker(data)
                }

                is CustomResult.OnError -> {
                    _emergencyResult.value = EmergencyResult.Error
                }
            }
        } catch (e: Exception) {
            _emergencyResult.value = EmergencyResult.ValidationError
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
            textBornDate.value = dateBirth
            textJoinDate.value = dateJoin
            principalPhone.value = phone
            secondaryPhone.value = phoneEmergency
            textNationality.value = nationality

            textAllergies.value = allergies?.takeIf { it.isNotEmpty() } ?: VALIDATE_NOT_ALLERGIES
            textDiseases.value = diseases?.takeIf { it.isNotEmpty() } ?: VALIDATE_NOT_DISEASES
            textBloodType.value = bloodType?.takeIf { it.isNotEmpty() } ?: VALIDATE_NOT_BLOOD_TYPE

        }
    }

    fun searchWorkers(searchBox: EditText) {
        searchBox.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val dni = searchBox.text.toString()
                validateWorkerByDNI(dni)
            }
            false
        }
    }

}