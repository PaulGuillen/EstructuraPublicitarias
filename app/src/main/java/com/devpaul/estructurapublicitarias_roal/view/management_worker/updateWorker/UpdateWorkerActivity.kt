package com.devpaul.estructurapublicitarias_roal.view.management_worker.updateWorker

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.ImageView
import android.widget.TextView
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

    private fun validateState(selectedViewIds: Array<View>, selectedImage: ImageView, selectedLabel: TextView, area: String) {
        if (!isSelected(area)) {
            val orangeColor = ContextCompat.getColor(this, R.color.orange_principal)
            val grayIconsColor = ContextCompat.getColor(this, R.color.color_gray_icons)

            val allViewIds = mapOf(
                INFORMATION_TECHNOLOGY_AREA to arrayOf(
                    binding.viewTopTI,
                    binding.viewBottomTI,
                    binding.viewLeftTI,
                    binding.viewRightTI
                ),
                WELDING_AREA to arrayOf(
                    binding.viewTopSI,
                    binding.viewBottomSI,
                    binding.viewLeftSI,
                    binding.viewRightSI
                ),
                COURT_AREA to arrayOf(
                    binding.viewTopCO,
                    binding.viewBottomCO,
                    binding.viewLeftCO,
                    binding.viewRightCO
                )
            )

            allViewIds.forEach { (key, viewIds) ->
                viewIds.forEach { it.setBackgroundColor(if (key == area) orangeColor else grayIconsColor) }
            }

            binding.checkBoxImageTI.setImageResource(if (area == INFORMATION_TECHNOLOGY_AREA) R.drawable.checked_box else R.drawable.not_checked_box)
            binding.checkBoxLabelTI.setTextColor(if (area == INFORMATION_TECHNOLOGY_AREA) orangeColor else grayIconsColor)

            binding.checkBoxImageSI.setImageResource(if (area == WELDING_AREA) R.drawable.checked_box else R.drawable.not_checked_box)
            binding.checkBoxLabelSI.setTextColor(if (area == WELDING_AREA) orangeColor else grayIconsColor)

            binding.checkBoxImageCO.setImageResource(if (area == COURT_AREA) R.drawable.checked_box else R.drawable.not_checked_box)
            binding.checkBoxLabelCO.setTextColor(if (area == COURT_AREA) orangeColor else grayIconsColor)

            setSelectionFlags(area)
        }
    }

    private fun isSelected(area: String) = when (area) {
        INFORMATION_TECHNOLOGY_AREA -> isSelectedTI
        WELDING_AREA -> isSelectedSI
        COURT_AREA -> isSelectedCO
        else -> false
    }

    private fun setSelectionFlags(area: String) {
        isSelectedTI = area == INFORMATION_TECHNOLOGY_AREA
        isSelectedSI = area == WELDING_AREA
        isSelectedCO = area == COURT_AREA
        areaSelected = when (area) {
            INFORMATION_TECHNOLOGY_AREA -> "1"
            WELDING_AREA -> "2"
            COURT_AREA -> "3"
            else -> areaSelected
        }
    }

    // Usage in your existing functions
    private fun validateStateTI() {
        validateState(
            arrayOf(
                binding.viewTopTI,
                binding.viewBottomTI,
                binding.viewLeftTI,
                binding.viewRightTI
            ),
            binding.checkBoxImageTI,
            binding.checkBoxLabelTI,
            INFORMATION_TECHNOLOGY_AREA
        )
    }

    private fun validateStateSO() {
        validateState(
            arrayOf(
                binding.viewTopSI,
                binding.viewBottomSI,
                binding.viewLeftSI,
                binding.viewRightSI
            ),
            binding.checkBoxImageSI,
            binding.checkBoxLabelSI,
            WELDING_AREA
        )
    }

    private fun validateStateCO() {
        validateState(
            arrayOf(
                binding.viewTopCO,
                binding.viewBottomCO,
                binding.viewLeftCO,
                binding.viewRightCO
            ),
            binding.checkBoxImageCO,
            binding.checkBoxLabelCO,
            COURT_AREA
        )
    }

    private fun setAreaSelection(area: String?) {
        area?.let {
            when (it) {
                INFORMATION_TECHNOLOGY_AREA -> validateState(
                    arrayOf(
                        binding.viewTopTI,
                        binding.viewBottomTI,
                        binding.viewLeftTI,
                        binding.viewRightTI
                    ),
                    binding.checkBoxImageTI,
                    binding.checkBoxLabelTI,
                    INFORMATION_TECHNOLOGY_AREA
                )

                WELDING_AREA -> validateState(
                    arrayOf(
                        binding.viewTopSI,
                        binding.viewBottomSI,
                        binding.viewLeftSI,
                        binding.viewRightSI
                    ),
                    binding.checkBoxImageSI,
                    binding.checkBoxLabelSI,
                    WELDING_AREA
                )

                COURT_AREA -> validateState(
                    arrayOf(
                        binding.viewTopCO,
                        binding.viewBottomCO,
                        binding.viewLeftCO,
                        binding.viewRightCO
                    ),
                    binding.checkBoxImageCO,
                    binding.checkBoxLabelCO,
                    COURT_AREA
                )
                else -> {
                   //Casuistica no contemplada
                }
            }
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