package com.devpaul.estructurapublicitarias_roal.view.validationepp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import com.devpaul.estructurapublicitarias_roal.data.models.response.ErrorModel
import com.devpaul.estructurapublicitarias_roal.data.models.response.ResponseHttp
import com.devpaul.estructurapublicitarias_roal.data.models.response.WorkersResponse
import com.devpaul.estructurapublicitarias_roal.databinding.ActivityValidationEppactivityBinding
import com.devpaul.estructurapublicitarias_roal.providers.WorkersProvider
import com.devpaul.estructurapublicitarias_roal.domain.utils.toolbarStyle
import com.devpaul.estructurapublicitarias_roal.view.base.BaseActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.File
import java.io.IOException

@SuppressLint("SourceLockedOrientationActivity")
class ValidationEPPActivity : BaseActivity() {

    lateinit var binding: ActivityValidationEppactivityBinding
    private var progressDialog: Dialog? = null
    var imageFile: File? = null
    var workersProvider = WorkersProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = ActivityValidationEppactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbarStyle(this@ValidationEPPActivity,binding.include.toolbar, "Validación EPP")
        setSupportActionBar(binding.include.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.imagePhoto.setOnClickListener { selectImage() }
    }

    private fun sendImageToBE() {
        CoroutineScope(Dispatchers.Default).launch {
            startImageForResult.let {
                imageFile?.let {
                    val imageInBase64 = getBase64ForUriAndPossiblyCrash(it.toUri())
                    val user = WorkersResponse(
                        photo = imageInBase64
                    )
                    workersProvider.validationEPP(user)?.enqueue(object : Callback<ResponseHttp> {
                        override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                            if (response.isSuccessful){
                                hideLoading()
                                if (response.code() == 200){
                                    Timber.d("ResponseValidation $response")
                                }
                            }else{
                                hideLoading()
                                val errorBody = response.errorBody()?.string()
                                val statusResponse = Gson().fromJson(errorBody, ErrorModel::class.java)

                                if (statusResponse.code == 400){

                                    statusResponse.requiredEquipment?.let { requiredEquipment ->
                                        Toast.makeText(this@ValidationEPPActivity,"Necesario : $requiredEquipment",Toast.LENGTH_LONG).show()
                                    }

                                    Timber.d("ResponseValidation error $statusResponse")

                                }else{
                                    Timber.d("ResponseValidation error $statusResponse")
                                }
                            }
                        }

                        override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                            hideLoading()
                        }
                    })
                }
            }
        }
    }

    private val startImageForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            dataResult(result)
        }

    private fun dataResult(result : ActivityResult){
        val resultCode = result.resultCode
        val data = result.data
        when(resultCode) {
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