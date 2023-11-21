package com.devpaul.estructurapublicitarias_roal.view.emergency

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.github.dhaval2404.imagepicker.ImagePicker
import com.devpaul.estructurapublicitarias_roal.domain.utils.toolbarStyle
import com.devpaul.estructurapublicitarias_roal.R
import com.devpaul.estructurapublicitarias_roal.data.models.entity.BY_DNI
import com.devpaul.estructurapublicitarias_roal.data.models.entity.BY_PHOTO
import com.devpaul.estructurapublicitarias_roal.data.models.entity.BY_QR
import com.devpaul.estructurapublicitarias_roal.data.models.entity.SearchMode
import com.devpaul.estructurapublicitarias_roal.databinding.ActivityEmergencyBinding
import com.devpaul.estructurapublicitarias_roal.domain.usecases.emergency.EmergencyResult
import com.devpaul.estructurapublicitarias_roal.domain.utils.MESSAGE_DATA_NOT_VALID
import com.devpaul.estructurapublicitarias_roal.domain.utils.SEARCH_TITLE_DNI
import com.devpaul.estructurapublicitarias_roal.domain.utils.ViewModelFactory
import com.devpaul.estructurapublicitarias_roal.domain.utils.applyCustomTextStyleToTextView
import com.devpaul.estructurapublicitarias_roal.domain.utils.startNewActivityWithBackAnimation
import com.devpaul.estructurapublicitarias_roal.view.HomeActivity
import com.devpaul.estructurapublicitarias_roal.view.base.BaseActivity
import com.google.zxing.integration.android.IntentIntegrator
import java.io.File

class EmergencyActivity : BaseActivity() {

    lateinit var binding: ActivityEmergencyBinding
    private var imageFile: File? = null
    private lateinit var viewModel: EmergencyViewModel

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = ActivityEmergencyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbarStyle(this@EmergencyActivity, binding.include.toolbar, SEARCH_TITLE_DNI, true, HomeActivity::class.java)

        viewModel =
            ViewModelProvider(this, ViewModelFactory(this, EmergencyViewModel::class.java))[EmergencyViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.btnSearchByDNI.setOnClickListener {
            switchSearchMode(BY_DNI)
        }

        binding.btnSearchByPhoto.setOnClickListener {
            switchSearchMode(BY_PHOTO)
        }

        binding.btnSearchByQR.setOnClickListener {
            switchSearchMode(BY_QR)
        }

        binding.textPrincipal.setOnClickListener { goToCall("phonePrincipal") }
        binding.textSecondary.setOnClickListener { goToCall("phoneSecondary") }

        binding.imagePhoto.setOnClickListener { selectImage() }
        binding.imageQR.setOnClickListener { initScanner() }

        viewModel.emergencyResult.observe(this) { result ->
            handleForgotPasswordResult(result)
        }

        viewModel.showLoadingDialog.observe(this) { showLoading ->
            if (showLoading) {
                showLoading()
            } else {
                hideLoading()
            }
        }
    }

    private fun handleForgotPasswordResult(result: EmergencyResult) {
        when (result) {
            is EmergencyResult.SuccessWorker -> {
                binding.linearLayoutNoDataFound.visibility = View.GONE
                binding.linearLayoutData.visibility = View.VISIBLE
                applyCustomTextStyleToTextView(binding.textPrincipal, result.data.message?.phone)
                applyCustomTextStyleToTextView(binding.textSecondary, result.data.message?.phoneEmergency)
            }

            is EmergencyResult.Error -> {
                binding.linearLayoutNoDataFound.visibility = View.VISIBLE
                binding.linearLayoutData.visibility = View.GONE
                clearForm()
            }

            is EmergencyResult.ValidationError -> {
                Toast.makeText(this, MESSAGE_DATA_NOT_VALID, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initScanner() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt(getString(R.string.text_need_dni_qr))
        integrator.setBeepEnabled(true)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                showToast(getString(R.string.action_cancelled_qr))
            } else {
                viewModel.validateWorkerByDNI(result.contents.toString())
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun goToCall(text: String) {

        when (text) {
            "phonePrincipal" -> {
                val number: String = binding.textPrincipal.text.toString()
                val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null))
                startActivity(intent)
            }

            "phoneSecondary" -> {
                val number: String = binding.textSecondary.text.toString()
                val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null))
                startActivity(intent)
            }

            else -> {
                /**Casuistica no contemplada*/
                TODO()
            }
        }
    }

    private fun switchSearchMode(searchMode: SearchMode) {
        toolbarStyle(this@EmergencyActivity, binding.include.toolbar, searchMode.title, true, HomeActivity::class.java)

        when (searchMode) {
            BY_DNI -> {
                binding.searchBox.visibility = View.VISIBLE
                binding.imagePhoto.visibility = View.GONE
                binding.imageQR.visibility = View.GONE
                binding.searchBox.text.clear()
            }

            BY_PHOTO -> {
                binding.searchBox.visibility = View.GONE
                binding.imagePhoto.visibility = View.VISIBLE
                binding.imageQR.visibility = View.GONE
                clearForm()
            }

            BY_QR -> {
                binding.searchBox.visibility = View.GONE
                binding.imagePhoto.visibility = View.GONE
                binding.imageQR.visibility = View.VISIBLE
            }
        }

        binding.linearLayoutData.visibility = View.GONE
        binding.linearLayoutNoDataFound.visibility = View.GONE

        if (searchMode == BY_DNI) {
            viewModel.searchWorkers(binding.searchBox)
            performSearch()
        }
    }

    private fun performSearch() {
        binding.searchBox.clearFocus()
        val `in`: InputMethodManager = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE)
                as InputMethodManager
        `in`.hideSoftInputFromWindow(binding.searchBox.windowToken, 0)
    }

    private fun clearForm() {
        imageFile = null
        binding.imagePhoto.setImageResource(R.drawable.ic_baseline_image_24)
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
                binding.imagePhoto.setImageURI(fileUri)
                val imageInBase64 = imageFile?.toUri()?.let { getBase64ForUriAndPossiblyCrash(it) }
                viewModel.validateImageByPhoto(imageFile, imageInBase64)
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

    override fun onBackPressed() {
        super.onBackPressed()
        startNewActivityWithBackAnimation(this@EmergencyActivity, HomeActivity::class.java)
    }

}
