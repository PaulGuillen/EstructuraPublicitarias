package com.devpaul.estructurapublicitarias_roal.view.management_worker

import android.app.Activity
import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.devpaul.estructurapublicitarias_roal.data.models.response.ResponseHttp
import com.devpaul.estructurapublicitarias_roal.data.models.response.WorkersResponse
import com.devpaul.estructurapublicitarias_roal.providers.WorkersProvider
import com.devpaul.estructurapublicitarias_roal.domain.utils.toolbarStyle
import com.devpaul.estructurapublicitarias_roal.R
import com.devpaul.estructurapublicitarias_roal.databinding.ActivityUpdateWorkerBinding
import com.devpaul.estructurapublicitarias_roal.view.base.BaseActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException

class UpdateWorkerActivity : BaseActivity() {

    private var workersProvider = WorkersProvider()
    lateinit var binding: ActivityUpdateWorkerBinding
    private var imageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateWorkerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbarStyle(this@UpdateWorkerActivity, binding.include.toolbar, "Actualización de datos")
        setSupportActionBar(binding.include.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        gettingDataPerUser()
        binding.imageViewUser.setOnClickListener { selectImage() }
        binding.btnActualizarData.setOnClickListener {
            updateWorkers()
        }
    }

    private fun gettingDataPerUser() {
        showLoading()
        val typeDNI = intent.getStringExtra("dni").toString()
        workersProvider.getWorkers(typeDNI)?.enqueue(object : Callback<WorkersResponse> {
            override fun onResponse(call: Call<WorkersResponse>, response: Response<WorkersResponse>) {
                if (response.body() != null) {
                    hideLoading()
                    val textIdentification = response.body()?.dni
                    val textName = response.body()?.name
                    val textFirstName = response.body()?.name
                    val textLastName = response.body()?.lastname
                    val textFullName = textName + "\n" + textLastName
                    val textDateBirth = response.body()?.dateBirth
                    val textDateJoin = response.body()?.dateJoin
                    val textArea = response.body()?.area
                    val textAllergies = response.body()?.allergies
                    val textPhone = response.body()?.phone
                    val textPhoto = response.body()?.photo
                    val textSecondaryPhone = response.body()?.phoneEmergency
                    val textDiseases = response.body()?.diseases
                    textPhoto.let {
                        Glide.with(this@UpdateWorkerActivity)
                            .load(textPhoto)
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
                    binding.textFirstName.text = textFirstName
                    binding.textLastName.text = textLastName
                    binding.textFullName.text = textFullName
                    binding.textDNI.text = textIdentification
                    binding.textAllergies.setText(textAllergies)
                    binding.textPhone.setText(textPhone)
                    binding.textPhoneEmergency.setText(textSecondaryPhone)
                    binding.textAllergies.setText(textAllergies)
                    binding.textArea.setText(textArea)
                    binding.textIllness.setText(textDiseases)
                    binding.textdateJoin.text = textDateJoin
                    binding.textdateBirth.text = textDateBirth
                } else {
                    hideLoading()
                }
            }

            override fun onFailure(call: Call<WorkersResponse>, t: Throwable) {
                hideLoading()
                Toast.makeText(this@UpdateWorkerActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun updateWorkers() {

        val dni = binding.textDNI.text.toString()
        val name = binding.textFirstName.text.toString()
        val lastname = binding.textLastName.text.toString()
        val dateBirth = binding.textdateBirth.text.toString()
        val dateJoin = binding.textdateJoin.text.toString()
        val area = binding.textArea.text.toString()
        val bloodType = binding.textBlood.text.toString()
        val diseases = binding.textIllness.text.toString()
        val allergies = binding.textAllergies.text.toString()
        val phone = binding.textPhone.text.toString()
        val phoneEmergency = binding.textPhoneEmergency.text.toString()
        val photo = binding.imageViewUser

        isValidForm(area, phone, phoneEmergency).let {
            showLoading()
            if (photo.drawable.constantState != ContextCompat.getDrawable(
                    this@UpdateWorkerActivity,
                    R.drawable.ic_baseline_image_24
                )?.constantState
            ) {
                startImageForResult.let {
                    if (imageFile != null) {
                        val imageBase = imageFile?.toUri()?.let { it1 -> getBase64ForUriAndPossiblyCrash(it1) }
                        val workerUser = WorkersResponse(
                            dni = dni,
                            name = name,
                            lastname = lastname,
                            dateBirth = dateBirth,
                            dateJoin = dateJoin,
                            area = area,
                            bloodType = bloodType,
                            diseases = diseases,
                            allergies = allergies,
                            phone = phone,
                            phoneEmergency = phoneEmergency,
                            photo = imageBase,
                            photoFormat = imageFile?.name
                        )
                        CoroutineScope(Dispatchers.Default).launch {
                            workersProvider.postWorkers(workerUser)?.enqueue(object : Callback<ResponseHttp> {
                                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                                    if (response.body()?.code == 200) {
                                        hideLoading()
                                        SweetAlertDialog(this@UpdateWorkerActivity, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText(getString(R.string.title_200_register_worker))
                                            .show()
                                    } else {
                                        hideLoading()
                                        SweetAlertDialog(this@UpdateWorkerActivity, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText(getString(R.string.title_error_register))
                                            .show()
                                    }
                                }

                                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                                    hideLoading()
                                    Toast.makeText(this@UpdateWorkerActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                                }

                            })
                        }
                    } else {
                        val workerUser = WorkersResponse(
                            dni = dni,
                            name = name,
                            lastname = lastname,
                            dateBirth = dateBirth,
                            dateJoin = dateJoin,
                            area = area,
                            bloodType = bloodType,
                            diseases = diseases,
                            allergies = allergies,
                            phone = phone,
                            phoneEmergency = phoneEmergency,
                        )
                        CoroutineScope(Dispatchers.Default).launch {
                            workersProvider.postWorkers(workerUser)?.enqueue(object : Callback<ResponseHttp> {
                                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                                    if (response.body()?.code == 200) {
                                        hideLoading()
                                        SweetAlertDialog(this@UpdateWorkerActivity, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText(getString(R.string.title_200_register_worker))
                                            .show()
                                    } else {
                                        hideLoading()
                                        SweetAlertDialog(this@UpdateWorkerActivity, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText(getString(R.string.title_error_register))
                                            .show()
                                    }
                                }

                                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                                    hideLoading()
                                    Toast.makeText(this@UpdateWorkerActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                                }

                            })
                        }
                    }
                }
            } else {
                SweetAlertDialog(this@UpdateWorkerActivity, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.title_404_image))
                    .setContentText(getString(R.string.subtitle_image_description))
                    .show();
            }
        }
    }

    private fun isValidForm(
        area: String,
        phone: String,
        phoneEmergency: String
    ): Boolean {

        if (area.isBlank()) {
            val messageError = getString(R.string.mandatory)
            toggleTextInputLayoutError(binding.textArea, messageError)
            return false
        }

        if (phone.isBlank() || phone.length < 9) {
            val messageError = getString(R.string.mandatory)
            toggleTextInputLayoutError(binding.textPhone, messageError)
            return false
        }

        if (phoneEmergency.isBlank() || phoneEmergency.length < 9) {
            val messageError = getString(R.string.mandatory)
            toggleTextInputLayoutError(binding.textPhoneEmergency, messageError)
            return false
        }

        return true
    }

    private fun toggleTextInputLayoutError(
        textInputEditText: TextInputEditText,
        msg: String?
    ) {
        textInputEditText.error = msg
        textInputEditText.isTextInputLayoutFocusedRectEnabled = msg != null
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
            error.printStackTrace()
            "Ha ocurrido un error"
        }
    }
}