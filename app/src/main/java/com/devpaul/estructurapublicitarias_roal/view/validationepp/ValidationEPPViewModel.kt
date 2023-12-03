package com.devpaul.estructurapublicitarias_roal.view.validationepp

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devpaul.estructurapublicitarias_roal.R
import com.devpaul.estructurapublicitarias_roal.data.models.entity.ValidationEPP
import com.devpaul.estructurapublicitarias_roal.data.models.request.ValidationEPPRequest
import com.devpaul.estructurapublicitarias_roal.data.repository.ValidationEPPRepository
import com.devpaul.estructurapublicitarias_roal.databinding.ItemCardviewValidateEppBinding
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.usecases.validateEPP.ValidationEPPResult
import com.devpaul.estructurapublicitarias_roal.domain.usecases.validateEPP.ValidationEPPUseCase
import com.devpaul.estructurapublicitarias_roal.domain.utils.SingletonError
import com.devpaul.estructurapublicitarias_roal.domain.utils.setSVGColorFromResource
import com.devpaul.estructurapublicitarias_roal.domain.utils.showDialogDetailsValidateEquipment
import com.devpaul.estructurapublicitarias_roal.view.base.BaseViewModel
import kotlinx.coroutines.launch
import java.io.File

class ValidationEPPViewModel(context: Context) : BaseViewModel() {

    private val _validateEPPResult = MutableLiveData<ValidationEPPResult>()

    val validateEPPResult: LiveData<ValidationEPPResult> = _validateEPPResult

    init {
        _validateEPPResult.value = ValidationEPPResult.DefaultColorsComponents
    }

    fun validateEPPService(centeredImage: ImageView?, subtitleImage: TextView?, imageFile: File?, imageInBase64: String?) {
        viewModelScope.launch {
            callServiceValidateEPP(centeredImage, subtitleImage, imageFile, imageInBase64)
        }
    }

    private suspend fun callServiceValidateEPP(
        centeredImage: ImageView?,
        subtitleImage: TextView?,
        imageFile: File?,
        imageInBase64: String?
    ) {

        val validateImageByPhotoRequest = ValidationEPPRequest(
            photo = imageInBase64,
        )

        if (imageFile == null) {
            _validateEPPResult.value = ValidationEPPResult.MissingImage
            return
        }

        centeredImage?.visibility = View.GONE
        subtitleImage?.visibility = View.GONE

        _showLoadingDialog.value = true

        try {
            val validateEPPRepository = ValidationEPPRepository()
            val validateUseCase = ValidationEPPUseCase(validateEPPRepository)
            when (val validateEPP = validateUseCase.validateEPP(validateImageByPhotoRequest)) {
                is CustomResult.OnSuccess -> {
                    val data = validateEPP.data
                    _validateEPPResult.value = ValidationEPPResult.Success(data)
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

    fun validateEquipment(
        data: ValidationEPP?,
        includeCardViewValidateEPP: ItemCardviewValidateEppBinding,
        validationEPPActivity: ValidationEPPActivity
    ) {
        val allEquipment = data?.allEquipment
        val wearingEquipment = data?.wearingEquipment
        val descriptionEquipment = data?.descriptionEquipment
        val areaEquipment = data?.area

        val helmet = allEquipment?.find { it.key.contentEquals("firstEPP") }?.value
        val gloves = allEquipment?.find { it.key.contentEquals("secondEPP") }?.value
        val glasses = allEquipment?.find { it.key.contentEquals("thirdEPP") }?.value
        val boots = allEquipment?.find { it.key.contentEquals("fourthEPP") }?.value
        val harness = allEquipment?.find { it.key.contentEquals("fifthEPP") }?.value
        val headphones = allEquipment?.find { it.key.contentEquals("sixEPP") }?.value
        val pants = allEquipment?.find { it.key.contentEquals("sevenEPP") }?.value
        val vest = allEquipment?.find { it.key.contentEquals("eightEPP") }?.value

        val elementMap = mapOf(
            helmet to includeCardViewValidateEPP.safetyHelmet,
            glasses to includeCardViewValidateEPP.safetyGlasses,
            gloves to includeCardViewValidateEPP.safetyGloves,
            boots to includeCardViewValidateEPP.safetyBoots,
            harness to includeCardViewValidateEPP.safetyHarness,
            headphones to includeCardViewValidateEPP.safetyHeadphones,
            pants to includeCardViewValidateEPP.safetyPants,
            vest to includeCardViewValidateEPP.safetyVest,
        )

        wearingEquipment?.forEach { item ->
            elementMap[item.key]?.let { imageButton ->
                val colorResource = if (item.value == false) {
                    R.color.red
                } else {
                    R.color.light_green_primary_dark
                }
                setSVGColorFromResource(imageButton, colorResource)
            }
        }

        val presentKeys = wearingEquipment?.map { it.key } ?: emptyList()

        val missingKeys = elementMap.keys - presentKeys.toSet()

        missingKeys.forEach { key ->
            elementMap[key]?.let { imageButton ->
                setSVGColorFromResource(imageButton, R.color.black)
            }
        }

        val colorBlueLight = ContextCompat.getColor(validationEPPActivity, R.color.color_blue_light)
        val cardDetailsTextView = includeCardViewValidateEPP.textViewCardDetails
        cardDetailsTextView.setBackgroundColor(colorBlueLight)

        if (cardDetailsTextView.background is ColorDrawable) {
            val currentColor = (cardDetailsTextView.background as ColorDrawable).color

            if (currentColor == colorBlueLight) {
                cardDetailsTextView.setOnClickListener {
                    showDialogDetailsValidateEquipment(validationEPPActivity, descriptionEquipment, areaEquipment)
                }
            }
        }
    }


}