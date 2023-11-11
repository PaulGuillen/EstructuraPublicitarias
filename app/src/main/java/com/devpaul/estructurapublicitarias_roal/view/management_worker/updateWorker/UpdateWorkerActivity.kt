package com.devpaul.estructurapublicitarias_roal.view.management_worker.updateWorker

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.devpaul.estructurapublicitarias_roal.domain.utils.toolbarStyle
import com.devpaul.estructurapublicitarias_roal.R
import com.devpaul.estructurapublicitarias_roal.databinding.ActivityUpdateWorkerBinding
import com.devpaul.estructurapublicitarias_roal.domain.usecases.updateWorker.UpdateWorkerResult
import com.devpaul.estructurapublicitarias_roal.domain.utils.*
import com.devpaul.estructurapublicitarias_roal.view.base.BaseActivity
import com.devpaul.estructurapublicitarias_roal.view.management_worker.managementWorker.ManagementWorkerActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import java.io.File
import java.io.IOException

class UpdateWorkerActivity : BaseActivity() {

    lateinit var binding: ActivityUpdateWorkerBinding
    private var imageFile: File? = null
    private lateinit var viewModel: UpdateWorkerViewModel
    private var isSelectedTI = false
    private var isSelectedSI = false
    private var isSelectedCO = false
    private var areaSelected: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateWorkerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbarStyle(
            this@UpdateWorkerActivity,
            binding.include.toolbar,
            TITLE_BAR_LAYOUT,
            true,
            ManagementWorkerActivity::class.java
        )

        viewModel =
            ViewModelProvider(this, ViewModelFactory(this, UpdateWorkerViewModel::class.java))[UpdateWorkerViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        binding.imageViewUser.setOnClickListener {
            selectImage()
        }

        binding.customCheckboxTI.setOnClickListener {
            validateStateTI()
        }
        binding.customCheckboxSI.setOnClickListener {
            validateStateSO()
        }

        binding.customCheckboxCO.setOnClickListener {
            validateStateCO()
        }

        viewModel.updateWorkerResult.observe(this) { result ->
            handleUpdateWorkerResult(result)
        }

        viewModel.showLoadingDialog.observe(this) { showLoading ->
            if (showLoading) {
                showLoading()
            } else {
                hideLoading()
            }
        }
        initData()
    }

    private fun initData() {
        val extras = intent.extras
        val document = extras?.getString("dni")
        val area = extras?.getString("area")

        viewModel.validateWorker(document.toString())

        setAreaSelection(area)
    }

    private fun validateStateTI() {
        if (!isSelectedTI) {
            isSelectedTI = true
            isSelectedSI = false
            isSelectedCO = false

            val orangeColor = ContextCompat.getColor(this, R.color.orange_principal)
            val grayIconsColor = ContextCompat.getColor(this, R.color.color_gray_icons)

            val tiViewIds = arrayOf(
                binding.viewTopTI,
                binding.viewBottomTI,
                binding.viewLeftTI,
                binding.viewRightTI
            )

            val siViewIds = arrayOf(
                binding.viewTopSI,
                binding.viewBottomSI,
                binding.viewLeftSI,
                binding.viewRightSI
            )

            val coViewIds = arrayOf(
                binding.viewTopCO,
                binding.viewBottomCO,
                binding.viewLeftCO,
                binding.viewRightCO
            )

            tiViewIds.forEach { it.setBackgroundColor(orangeColor) }
            siViewIds.forEach { it.setBackgroundColor(grayIconsColor) }
            coViewIds.forEach { it.setBackgroundColor(grayIconsColor) }

            binding.checkBoxImageTI.setImageResource(R.drawable.checked_box)
            binding.checkBoxLabelTI.setTextColor(orangeColor)

            binding.checkBoxImageSI.setImageResource(R.drawable.not_checked_box)
            binding.checkBoxLabelSI.setTextColor(grayIconsColor)

            binding.checkBoxImageCO.setImageResource(R.drawable.not_checked_box)
            binding.checkBoxLabelCO.setTextColor(grayIconsColor)

            areaSelected = "1"
        }
    }

    private fun validateStateSO() {
        if (!isSelectedSI) {
            isSelectedSI = true
            isSelectedTI = false
            isSelectedCO = false

            val orangeColor = ContextCompat.getColor(this, R.color.orange_principal)
            val grayIconsColor = ContextCompat.getColor(this, R.color.color_gray_icons)

            val siViewIds = arrayOf(
                binding.viewTopSI,
                binding.viewBottomSI,
                binding.viewLeftSI,
                binding.viewRightSI
            )

            val tiViewIds = arrayOf(
                binding.viewTopTI,
                binding.viewBottomTI,
                binding.viewLeftTI,
                binding.viewRightTI
            )

            val coViewIds = arrayOf(
                binding.viewTopCO,
                binding.viewBottomCO,
                binding.viewLeftCO,
                binding.viewRightCO
            )

            siViewIds.forEach { it.setBackgroundColor(orangeColor) }
            tiViewIds.forEach { it.setBackgroundColor(grayIconsColor) }
            coViewIds.forEach { it.setBackgroundColor(grayIconsColor) }

            binding.checkBoxImageSI.setImageResource(R.drawable.checked_box)
            binding.checkBoxLabelSI.setTextColor(orangeColor)

            binding.checkBoxImageTI.setImageResource(R.drawable.not_checked_box)
            binding.checkBoxLabelTI.setTextColor(grayIconsColor)

            binding.checkBoxImageCO.setImageResource(R.drawable.not_checked_box)
            binding.checkBoxLabelCO.setTextColor(grayIconsColor)

            areaSelected = "2"
        }
    }

    private fun validateStateCO() {
        if (!isSelectedCO) {
            isSelectedCO = true
            isSelectedSI = false
            isSelectedTI = false

            val orangeColor = ContextCompat.getColor(this, R.color.orange_principal)
            val grayIconsColor = ContextCompat.getColor(this, R.color.color_gray_icons)

            val siViewIds = arrayOf(
                binding.viewTopSI,
                binding.viewBottomSI,
                binding.viewLeftSI,
                binding.viewRightSI
            )

            val tiViewIds = arrayOf(
                binding.viewTopTI,
                binding.viewBottomTI,
                binding.viewLeftTI,
                binding.viewRightTI
            )

            val coViewIds = arrayOf(
                binding.viewTopCO,
                binding.viewBottomCO,
                binding.viewLeftCO,
                binding.viewRightCO
            )

            coViewIds.forEach { it.setBackgroundColor(orangeColor) }
            siViewIds.forEach { it.setBackgroundColor(grayIconsColor) }
            tiViewIds.forEach { it.setBackgroundColor(grayIconsColor) }

            binding.checkBoxImageCO.setImageResource(R.drawable.checked_box)
            binding.checkBoxLabelCO.setTextColor(orangeColor)

            binding.checkBoxImageSI.setImageResource(R.drawable.not_checked_box)
            binding.checkBoxLabelSI.setTextColor(grayIconsColor)

            binding.checkBoxImageTI.setImageResource(R.drawable.not_checked_box)
            binding.checkBoxLabelTI.setTextColor(grayIconsColor)

            areaSelected = "3"
        }
    }

    private fun handleUpdateWorkerResult(result: UpdateWorkerResult) {
        when (result) {
            is UpdateWorkerResult.WorkerDataReceived -> {
                result.worker.message?.photo.let {
                    Glide.with(this@UpdateWorkerActivity)
                        .load(result.worker.message?.photo)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .apply(
                            RequestOptions.centerCropTransform()
                                .placeholder(R.drawable.ic_baseline_supervised_user_circle_24)
                                .error(R.drawable.ic_baseline_supervised_user_circle_24)
                                .priority(Priority.HIGH)
                        )
                        .into(binding.imageViewUser)
                }
            }

            is UpdateWorkerResult.UpdateWorker -> {
                if (imageFile == null) {
                    viewModel.updateWorkerNoImage(areaSelected)
                } else {
                    val imageInBase64 = imageFile?.toUri()?.let { getBase64ForUriAndPossiblyCrash(it) }
                    viewModel.updateWorkerImage(imageFile, imageInBase64, areaSelected)
                }
            }

            is UpdateWorkerResult.UpdateWorkerSuccess -> {
                SweetAlertDialog(this@UpdateWorkerActivity, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(getString(R.string.title_200_update))
                    .show()
            }

            is UpdateWorkerResult.Error -> {
                showCustomDialogErrorSingleton(this,
                    TITLE_ERROR_MS_UPDATE_WORKER,
                    result.subTitle,
                    result.code,
                    getString(R.string.dialog_singleton_text_button_accept),
                    onClickListener = {
                        startNewActivityWithBackAnimation(this, ManagementWorkerActivity::class.java)
                    })
            }

            is UpdateWorkerResult.ValidationError -> {
                Toast.makeText(this, MESSAGE_DATA_NOT_VALID, Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun setAreaSelection(area: String?) {
        when (area) {
            "TI" -> validateStateTI()
            "Soldadura" -> validateStateSO()
            "Corte" -> validateStateCO()
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
                imageFile = fileUri?.path?.let { File(it) }
                binding.imageViewUser.setImageURI(fileUri)
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
            error.printStackTrace()
            "Ha ocurrido un error"
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startNewActivityWithBackAnimation(this@UpdateWorkerActivity, ManagementWorkerActivity::class.java)
    }
}