package com.devpaul.estructurapublicitarias_roal.view.emergency

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import com.devpaul.estructurapublicitarias_roal.data.models.response.WorkersResponse
import com.devpaul.estructurapublicitarias_roal.providers.WorkersProvider
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.devpaul.estructurapublicitarias_roal.domain.utils.toolbarStyle
import com.devpaul.estructurapublicitarias_roal.R
import com.devpaul.estructurapublicitarias_roal.databinding.ActivityEmergencyBinding
import com.devpaul.estructurapublicitarias_roal.domain.utils.deleteCache
import com.devpaul.estructurapublicitarias_roal.view.base.BaseActivity
import com.google.zxing.integration.android.IntentIntegrator
import java.io.File
import java.io.IOException

class EmergencyActivity : BaseActivity() {

    lateinit var binding: ActivityEmergencyBinding
    private var workersProvider = WorkersProvider()
    private var progressDialog: Dialog? = null
    private var imageFile: File? = null

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = ActivityEmergencyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbarStyle(this@EmergencyActivity, binding.include.toolbar, "Emergencia")
        setSupportActionBar(binding.include.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnSearchByDNI.setOnClickListener { searchingByDNI() }
        binding.btnSearchByPhoto.setOnClickListener { searchingByPhoto() }
        binding.btnSearchByQR.setOnClickListener { searchingByQR() }

        binding.btnPhonePrincipal.setOnClickListener { goToCall("phonePrincipal") }
        binding.btnPhoneSecondary.setOnClickListener { goToCall("phoneSecondary") }

        binding.imagePhoto.setOnClickListener { selectImage() }
        binding.imageQR.setOnClickListener { initScanner() }

    }

    private fun initScanner() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Porfavor verifica que el QR solo sea el DNI")
        //integrator.setTorchEnabled(true)
        integrator.setBeepEnabled(true)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show()
            } else {
                showLoading()
                getWorkers(result.contents.toString())
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

    private fun searchingByDNI() {
        toolbarStyle(this@EmergencyActivity, binding.include.toolbar, "Busquedad por DNI")
        binding.searchBox.visibility = View.VISIBLE
        binding.linearLayoutNoDataFound.visibility = View.GONE
        binding.imagePhoto.visibility = View.GONE
        binding.imageQR.visibility = View.GONE
        searchWorkers()
    }

    private fun searchingByPhoto() {
        toolbarStyle(this@EmergencyActivity, binding.include.toolbar, "Busquedad por FOTO")
        binding.searchBox.visibility = View.GONE
        binding.imagePhoto.visibility = View.VISIBLE
        binding.linearLayoutNoDataFound.visibility = View.GONE
        binding.imageQR.visibility = View.GONE
        binding.linearDNI.visibility = View.GONE
    }

    private fun searchingByQR() {
        toolbarStyle(this@EmergencyActivity, binding.include.toolbar, "Busquedad por QR")
        binding.searchBox.visibility = View.GONE
        binding.imagePhoto.visibility = View.GONE
        binding.linearLayoutNoDataFound.visibility = View.GONE
        binding.imageQR.visibility = View.VISIBLE
        binding.linearDNI.visibility = View.GONE
    }

    private fun searchWorkers() {
        binding.searchBox.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val dni = binding.searchBox.text.toString()
                showLoading()
                getWorkers(dni)
                performSearch()
            }
            false
        }
    }

    private fun getWorkers(dni: String) {
        workersProvider.getWorkers(dni)?.enqueue(object : Callback<WorkersResponse> {
            override fun onResponse(call: Call<WorkersResponse>, response: Response<WorkersResponse>) {
                if (response.body() != null) {
                    hideLoading()
                    binding.linearDNI.visibility = View.VISIBLE
                    binding.linearLayoutNoDataFound.visibility = View.GONE

                    /**Data base*/
                    val textIdentification = response.body()?.dni
                    val textName = response.body()?.name
                    val textLastName = response.body()?.lastname
                    val textFullName = "$textName $textLastName"

                    /**Celulares*/
                    val textPrincipalPhone = response.body()?.phone
                    val textSecondaryPhone = response.body()?.phoneEmergency

                    val textArea = response.body()?.area
                    val textDateBirth = response.body()?.dateBirth
                    val textJoinDate = response.body()?.dateJoin
                    val textBloodType = response.body()?.bloodType
                    val textDiseases = response.body()?.diseases
                    val textAllergies = response.body()?.allergies

                    if (textAllergies.isNullOrEmpty()) {
                        binding.textAllergies.text = "No alergías"
                    } else {
                        binding.textAllergies.text = textAllergies
                    }
                    if (textDiseases.isNullOrEmpty()) {
                        binding.textDiseases.text = "No enfermedades"
                    } else {
                        binding.textDiseases.text = textDiseases
                    }
                    if (textBloodType.isNullOrEmpty()) {
                        binding.textBloodType.text = "No grupo"
                    } else {
                        binding.textBloodType.text = textBloodType
                    }

                    binding.textDNI.text = textIdentification
                    binding.textFullName.text = textFullName
                    binding.textArea.text = textArea
                    binding.textBornDate.text = textDateBirth
                    binding.textJoinDate.text = textJoinDate
                    binding.textPrincipal.text = textPrincipalPhone
                    binding.textSecondary.text = textSecondaryPhone

                } else {
                    hideLoading()
                    binding.linearDNI.visibility = View.GONE
                    binding.linearLayoutNoDataFound.visibility = View.VISIBLE
                    clearForm()
                }
            }

            override fun onFailure(call: Call<WorkersResponse>, t: Throwable) {
                hideLoading()
                Toast.makeText(this@EmergencyActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun sendImageToBE() {
        CoroutineScope(Dispatchers.Default).launch {
            startImageForResult.let {
                imageFile?.let {
                    val imageInBase64 = getBase64ForUriAndPossiblyCrash(it.toUri())
                    val newFileName = String.format("%s.jpg", imageFile?.nameWithoutExtension)
                    val user = WorkersResponse(
                        photo = imageInBase64,
                        photoFormat = newFileName
                    )
                    workersProvider.consultByPhoto(user)?.enqueue(object : Callback<WorkersResponse> {
                        override fun onResponse(call: Call<WorkersResponse>, response: Response<WorkersResponse>) {
                            if (response.code() == 200) {
                                hideLoading()
                                binding.linearDNI.visibility = View.VISIBLE
                                val dni = response.body()?.dni.toString()
                                getWorkers(dni)
                            } else {
                                hideLoading()
                                binding.linearDNI.visibility = View.GONE
                                binding.linearLayoutNoDataFound.visibility = View.VISIBLE
                                clearForm()
                            }
                            deleteCache(this@EmergencyActivity)
                        }

                        override fun onFailure(call: Call<WorkersResponse>, t: Throwable) {
                            hideLoading()
                            binding.linearDNI.visibility = View.GONE
                            Toast.makeText(this@EmergencyActivity, "No se pudo guardar el dato", Toast.LENGTH_LONG).show()
                        }
                    })
                }
            }
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
                showLoading()
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

}
