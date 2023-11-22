package com.devpaul.estructurapublicitarias_roal.view.validationepp

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.devpaul.estructurapublicitarias_roal.R
import com.devpaul.estructurapublicitarias_roal.data.models.entity.ValidationEPP
import com.devpaul.estructurapublicitarias_roal.databinding.ActivityValidationEppactivityBinding
import com.devpaul.estructurapublicitarias_roal.domain.usecases.validateEPP.ValidationEPPResult
import com.devpaul.estructurapublicitarias_roal.domain.utils.*
import com.devpaul.estructurapublicitarias_roal.view.HomeActivity
import com.devpaul.estructurapublicitarias_roal.view.base.BaseActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.coroutines.*
import java.io.File

@SuppressLint("SourceLockedOrientationActivity")
class ValidationEPPActivity : BaseActivity() {

    lateinit var binding: ActivityValidationEppactivityBinding
    private lateinit var viewModel: ValidationEPPViewModel
    private var imageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityValidationEppactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbarStyle(this@ValidationEPPActivity, binding.include.toolbar, "Validación EPP", true, HomeActivity::class.java)

        viewModel =
            ViewModelProvider(this, ViewModelFactory(this, ValidationEPPViewModel::class.java))[ValidationEPPViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.validateEPPResult.observe(this) { result ->
            handleValidateEPPResult(result)
        }

        viewModel.showLoadingDialog.observe(this) { showLoading ->
            if (showLoading) {
                showLoading()
            } else {
                hideLoading()
            }
        }

        binding.imagePhoto.setOnClickListener { selectImage() }
    }

    private fun handleValidateEPPResult(result: ValidationEPPResult) {
        when (result) {
            is ValidationEPPResult.Success -> {
                validateEquipment(result.data)
            }

            is ValidationEPPResult.Error -> {
                showCustomDialogErrorSingleton(this,
                    result.title,
                    result.subTitle,
                    result.code,
                    getString(R.string.dialog_singleton_text_button_accept),
                    onClickListener = {})
            }

            is ValidationEPPResult.ExceptionError -> {
                Toast.makeText(this, result.exception.toString(), Toast.LENGTH_SHORT).show()
            }

            is ValidationEPPResult.MissingImage -> {
                Toast.makeText(this, MESSAGE_DATA_NOT_VALID, Toast.LENGTH_SHORT).show()
            }

            is ValidationEPPResult.DefaultColorsComponents -> {
                setDefaultColorEquipment()
            }
        }
    }

    private val startImageForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            dataResult(result)
        }

    private fun dataResult(result: ActivityResult) {
        val resultCode = result.resultCode
        val data = result.data
        when (resultCode) {
            Activity.RESULT_OK -> {
                val fileUri = data?.data
                imageFile = fileUri?.path?.let {
                    File(it)
                }
                binding.imagePhoto.setImageURI(fileUri)
                val imageInBase64 = imageFile?.toUri()?.let { getBase64ForUriAndPossiblyCrash(it) }
                viewModel.validateEPPService(binding.centeredImage, binding.subtitleImage, imageFile, imageInBase64)
            }

            ImagePicker.RESULT_ERROR -> {
                hideLoading()
            }

            else -> {
                /**Si se cierra la vista*/
            }
        }
    }

    private fun selectImage() {
        ImagePicker.with(this).crop().compress(1024).maxResultSize(1080, 1080).createIntent { intent -> startImageForResult.launch(intent) }
    }

    private fun setDefaultColorEquipment() {
        setSVGColorFromResource(binding.includeCardViewValidateEPP.safetyHelmet, R.color.color_gray_items)
        setSVGColorFromResource(binding.includeCardViewValidateEPP.safetyGloves, R.color.color_gray_items)
        setSVGColorFromResource(binding.includeCardViewValidateEPP.safetyGlasses, R.color.color_gray_items)
        setSVGColorFromResource(binding.includeCardViewValidateEPP.safetyBoots, R.color.color_gray_items)
        setSVGColorFromResource(binding.includeCardViewValidateEPP.safetyHeadphones, R.color.color_gray_items)
        setSVGColorFromResource(binding.includeCardViewValidateEPP.safetyHarness, R.color.color_gray_items)
        setSVGColorFromResource(binding.includeCardViewValidateEPP.safetyPants, R.color.color_gray_items)
        setSVGColorFromResource(binding.includeCardViewValidateEPP.safetyVest, R.color.color_gray_items)
    }

    private fun validateEquipment(data: ValidationEPP?) {
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
            helmet to binding.includeCardViewValidateEPP.safetyHelmet,
            glasses to binding.includeCardViewValidateEPP.safetyGlasses,
            gloves to binding.includeCardViewValidateEPP.safetyGloves,
            boots to binding.includeCardViewValidateEPP.safetyBoots,
            harness to binding.includeCardViewValidateEPP.safetyHarness,
            headphones to binding.includeCardViewValidateEPP.safetyHeadphones,
            pants to binding.includeCardViewValidateEPP.safetyPants,
            vest to binding.includeCardViewValidateEPP.safetyVest,
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

        val colorBlueLight = ContextCompat.getColor(this, R.color.color_blue_light)
        val cardDetailsTextView = binding.includeCardViewValidateEPP.textViewCardDetails
        cardDetailsTextView.setBackgroundColor(colorBlueLight)

        if (cardDetailsTextView.background is ColorDrawable) {
            val currentColor = (cardDetailsTextView.background as ColorDrawable).color

            if (currentColor == colorBlueLight) {
                cardDetailsTextView.setOnClickListener {
                    showDialogDetailsValidateEquipment(this@ValidationEPPActivity, descriptionEquipment, areaEquipment)
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startNewActivityWithBackAnimation(this@ValidationEPPActivity, HomeActivity::class.java)
    }

}