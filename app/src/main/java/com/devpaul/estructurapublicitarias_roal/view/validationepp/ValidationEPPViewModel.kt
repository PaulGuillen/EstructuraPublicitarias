package com.devpaul.estructurapublicitarias_roal.view.validationepp

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devpaul.estructurapublicitarias_roal.data.models.request.ValidationEPPRequest
import com.devpaul.estructurapublicitarias_roal.data.repository.ValidationEPPRepository
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.usecases.validateEPP.ValidationEPPResult
import com.devpaul.estructurapublicitarias_roal.domain.usecases.validateEPP.ValidationEPPUseCase
import com.devpaul.estructurapublicitarias_roal.domain.utils.SingletonError
import com.devpaul.estructurapublicitarias_roal.view.base.BaseViewModel
import kotlinx.coroutines.launch
import java.io.File

class ValidationEPPViewModel(context: Context) : BaseViewModel() {

    private val _validateEPPResult = MutableLiveData<ValidationEPPResult>()

    val validateEPPResult: LiveData<ValidationEPPResult> = _validateEPPResult


    init {
        _validateEPPResult.value = ValidationEPPResult.DefaultColorsComponents
    }

    private fun validateEPPService(imageFile: File?, imageInBase64: String?) {
        viewModelScope.launch {
            callServiceValidateEPP(imageFile, imageInBase64)
        }
    }

    private suspend fun callServiceValidateEPP(imageFile: File?, imageInBase64: String?) {

        val validateImageByPhotoRequest = ValidationEPPRequest(
            photo = imageInBase64,
        )

        if (imageFile == null) {
            _validateEPPResult.value = ValidationEPPResult.MissingImage
            return
        }

        _showLoadingDialog.value = true

        try {
            val validateEPPRepository = ValidationEPPRepository()
            val validateUseCase = ValidationEPPUseCase(validateEPPRepository)
            when (val validateEPP = validateUseCase.validateEPP(validateImageByPhotoRequest)) {
                is CustomResult.OnSuccess -> {
                    val data = validateEPP.data

                }

                is CustomResult.OnError -> {
                    val error = validateEPP.error
                    _validateEPPResult.value = ValidationEPPResult.Error(
                        error.code ?: SingletonError.code,
                        error.title ?: SingletonError.title,
                        error.subtitle ?: SingletonError.subTitle
                    )
                }
            }
        } catch (e: Exception) {
            _validateEPPResult.value = ValidationEPPResult.ExceptionError(e)
        } finally {
            _showLoadingDialog.value = false
        }
    }

}