package com.devpaul.estructurapublicitarias_roal.view.validationepp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.net.toUri
import com.devpaul.estructurapublicitarias_roal.R
import com.devpaul.estructurapublicitarias_roal.data.models.request.ValidationEPPRequest
import com.devpaul.estructurapublicitarias_roal.data.repository.ValidationEPPRepository
import com.devpaul.estructurapublicitarias_roal.databinding.ActivityValidationEppactivityBinding
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.usecases.ValidationEPPUseCase
import com.devpaul.estructurapublicitarias_roal.domain.utils.SingletonError
import com.devpaul.estructurapublicitarias_roal.domain.utils.setSVGColorFromResource
import com.devpaul.estructurapublicitarias_roal.domain.utils.showCustomDialog
import com.devpaul.estructurapublicitarias_roal.domain.utils.startNewActivityWithBackAnimation
import com.devpaul.estructurapublicitarias_roal.domain.utils.toolbarStyle
import com.devpaul.estructurapublicitarias_roal.view.HomeActivity
import com.devpaul.estructurapublicitarias_roal.view.base.BaseActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.IOException

@SuppressLint("SourceLockedOrientationActivity")
class ValidationEPPActivity : BaseActivity() {

    lateinit var binding: ActivityValidationEppactivityBinding
    private var imageFile: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = ActivityValidationEppactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbarStyle(this@ValidationEPPActivity, binding.include.toolbar, "Validación EPP", true, HomeActivity::class.java)
        binding.imagePhoto.setOnClickListener { selectImage() }


        setSVGColorFromResource(binding.safetyHelmet, R.color.red)
        setSVGColorFromResource(binding.safetyBoots, R.color.green_checked)
        setSVGColorFromResource(binding.safetyGloves, R.color.green_checked)
    }

    private fun sendImageToBE() {
        showLoading()
        CoroutineScope(Dispatchers.Default).launch {
            try {
                startImageForResult.let {
                    imageFile?.let {
                        val imageInBase64 = getBase64ForUriAndPossiblyCrash(it.toUri())
                        val validateEPP = ValidationEPPRequest().apply {
                            photo = imageInBase64
                        }

                        val validateEPPRepository = ValidationEPPRepository()
                        val validateUseCase = ValidationEPPUseCase(this@ValidationEPPActivity, validateEPPRepository)
                        val requestValidateEPPService = validateUseCase.validateEPP(validateEPP)

                        withContext(Dispatchers.Main) {

                            hideLoading()

                            when (requestValidateEPPService) {
                                is CustomResult.OnSuccess -> {
                                    val data = requestValidateEPPService.data

                                }

                                is CustomResult.OnError -> {

                                    val codeState = SingletonError.code
                                    val titleState = SingletonError.title
                                    val subTitleState = if (SingletonError.subTitle.isNullOrEmpty()) {
                                        "No data"
                                    } else {
                                        SingletonError.subTitle
                                    }

                                    showCustomDialog(
                                        this@ValidationEPPActivity,
                                        titleState,
                                        subTitleState,
                                        codeState,
                                        "Aceptar",
                                        onClickListener = {})
                                }
                            }
                        }

                    }
                }
            } catch (e: Exception) {
                Timber.d("Response $e")
            }

        }
    }

    private fun hideViewItems() {
        binding.centeredImage.visibility = View.GONE
        binding.subtitleImage.visibility = View.GONE
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
                hideViewItems()
                sendImageToBE()
            }

            ImagePicker.RESULT_ERROR -> {
                hideLoading()
                /**Causistica a contemplar**/
            }

            else -> {
                /**Si se cierra la vista*/
            }
        }
    }

    private fun selectImage() {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .createIntent { intent -> startImageForResult.launch(intent) }
    }

    private fun getBase64ForUriAndPossiblyCrash(uri: Uri): String {
        return try {
            val bytes = contentResolver.openInputStream(uri)?.readBytes()
            Base64.encodeToString(bytes, Base64.DEFAULT)
        } catch (error: IOException) {
            error.printStackTrace() // This exception always occurs
            "Ha ocurrido un error"
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startNewActivityWithBackAnimation(this@ValidationEPPActivity, HomeActivity::class.java)
    }
}