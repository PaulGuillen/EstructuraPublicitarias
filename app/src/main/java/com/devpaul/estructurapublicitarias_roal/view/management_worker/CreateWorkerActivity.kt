package com.devpaul.estructurapublicitarias_roal.view.management_worker

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import cn.pedant.SweetAlert.SweetAlertDialog
import com.devpaul.estructurapublicitarias_roal.R
import com.devpaul.estructurapublicitarias_roal.data.models.response.ResponseHttp
import com.devpaul.estructurapublicitarias_roal.data.models.response.WorkersResponse
import com.devpaul.estructurapublicitarias_roal.databinding.ActivityCreateWorkerBinding
import com.devpaul.estructurapublicitarias_roal.domain.utils.applyCustomTextStyleToTextView
import com.devpaul.estructurapublicitarias_roal.domain.utils.deleteCache
import com.devpaul.estructurapublicitarias_roal.domain.utils.isValidPhoneNumber
import com.devpaul.estructurapublicitarias_roal.domain.utils.showErrorAlert
import com.devpaul.estructurapublicitarias_roal.domain.utils.startNewActivityWithBackAnimation
import com.devpaul.estructurapublicitarias_roal.domain.utils.toolbarStyle
import com.devpaul.estructurapublicitarias_roal.providers.WorkersProvider
import com.devpaul.estructurapublicitarias_roal.view.base.BaseActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@SuppressLint("SourceLockedOrientationActivity")
class CreateWorkerActivity : BaseActivity() {

    lateinit var binding: ActivityCreateWorkerBinding
    private var postWorkersProvider = WorkersProvider()
    private var imageFile: File? = null
    private var isSelectedTI = false
    private var isSelectedSI = false
    private var isSelectedCO = false
    private var documentType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = ActivityCreateWorkerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbarStyle(
            this@CreateWorkerActivity,
            binding.include.toolbar,
            "Registro de trabajador",
            true,
            ManagementWorkerActivity::class.java
        )

        binding.btnRegistrarTrabajador.setOnClickListener { creatingWorkers() }
        binding.imageViewUser.setOnClickListener { selectImage() }
        binding.btnCalendar.setOnClickListener { calendarView("textBornDate") }
        binding.btnCalendarJoin.setOnClickListener { calendarView("textJoinDate") }
        binding.includedCheckbox.customCheckboxTI.setOnClickListener {
            validateStateTI()
        }
        binding.includedCheckbox.customCheckboxSI.setOnClickListener {
            validateStateSO()
        }

        binding.includedCheckbox.customCheckboxCO.setOnClickListener {
            validateStateCO()
        }

        binding.textBornDate.addTextChangedListener(DateTextWatcher())
        binding.textJoinDate.addTextChangedListener(DateTextWatcher())

        binding.textLegendArea.setOnClickListener {
            dialogLegendArea()
        }

        binding.viewBloodType.setOnClickListener {
            showTextBloodyType()
        }

        binding.viewGender.setOnClickListener {
            showGender()
        }

        binding.viewNationality.setOnClickListener {
            showTextNationality()
        }

        applyCustomTextStyleToTextView(binding.textLegendArea, "Leyenda")
    }

    private fun dialogLegendArea() {
        val tiText = "TI = Tecnologia de informacion"
        val soText = "SO = Soldadura"
        val coText = "CO = Corte"

        val message = "$tiText\n$soText\n$coText"

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Leyenda")
            .setMessage(message)
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
            }

        val dialog = builder.create()
        dialog.show()
    }

    private fun showTextNationality() {

        val arrayList = ArrayList<String>().apply {
            add("Argentina")
            add("Brasil")
            add("Chile")
            add("Colombia")
            add("Ecuador")
            add("Perú")
            add("Venezuela")
            add("Uruguay")
            add("Paraguay")
            add("Bolivia")
        }

        val dialog = Dialog(this@CreateWorkerActivity)
        dialog.setContentView(R.layout.dialog_searchable_spinner)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        val editText: EditText = dialog.findViewById(R.id.edit_text)
        val listView: ListView = dialog.findViewById(R.id.list_view)
        val textTitle: TextView = dialog.findViewById(R.id.titleSearchView)

        val adapter = ArrayAdapter(this@CreateWorkerActivity, android.R.layout.simple_list_item_1, arrayList)
        listView.adapter = adapter
        textTitle.text = "Selecciona una nación"

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                adapter.filter.filter(charSequence)
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        listView.setOnItemClickListener { _, _, i, _ ->
            binding.viewNationality.text = adapter.getItem(i)
            dialog.dismiss()
        }

    }

    private fun showTextBloodyType() {

        val arrayList = ArrayList<String>().apply {
            add("A+")
            add("A-")
            add("B+")
            add("B-")
            add("AB+")
            add("AB-")
            add("O+")
            add("O-")
        }

        val dialog = Dialog(this@CreateWorkerActivity)
        dialog.setContentView(R.layout.dialog_searchable_spinner)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        val editText: EditText = dialog.findViewById(R.id.edit_text)
        val listView: ListView = dialog.findViewById(R.id.list_view)
        val textTitle: TextView = dialog.findViewById(R.id.titleSearchView)

        val adapter = ArrayAdapter(this@CreateWorkerActivity, android.R.layout.simple_list_item_1, arrayList)
        listView.adapter = adapter
        textTitle.text = "Tipo de Sangre"

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                adapter.filter.filter(charSequence)
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        listView.setOnItemClickListener { _, _, i, _ ->
            binding.viewBloodType.text = adapter.getItem(i)
            dialog.dismiss()
        }

    }

    private fun showGender() {

        val arrayList = ArrayList<String>().apply {
            add("Masculino")
            add("Femenino")
            add("Otros")
        }

        val dialog = Dialog(this@CreateWorkerActivity)
        dialog.setContentView(R.layout.dialog_searchable_spinner)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        val textTitle: TextView = dialog.findViewById(R.id.titleSearchView)
        val editText: EditText = dialog.findViewById(R.id.edit_text)
        val listView: ListView = dialog.findViewById(R.id.list_view)
        val adapter = ArrayAdapter(this@CreateWorkerActivity, android.R.layout.simple_list_item_1, arrayList)

        listView.adapter = adapter
        textTitle.text = "Selecciona el género"

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                adapter.filter.filter(charSequence)
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        listView.setOnItemClickListener { _, _, i, _ ->
            binding.viewGender.text = adapter.getItem(i)
            dialog.dismiss()
        }

    }

    inner class DateTextWatcher : TextWatcher {
        private val separator = "-"
        private val positionsToAddSeparator = listOf(4, 7)

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable) {
            if (s.isNotEmpty()) {
                for (position in positionsToAddSeparator) {
                    if (s.length > position && s[position] != separator[0]) {
                        s.insert(position, separator)
                    }
                }
            }
        }
    }


    private fun validateStateTI() {
        if (!isSelectedTI) {
            isSelectedTI = true
            isSelectedSI = false
            isSelectedCO = false

            val orangeColor = ContextCompat.getColor(this, R.color.orange_principal)
            val grayIconsColor = ContextCompat.getColor(this, R.color.color_gray_icons)

            val tiViewIds = arrayOf(
                binding.includedCheckbox.viewTopTI,
                binding.includedCheckbox.viewBottomTI,
                binding.includedCheckbox.viewLeftTI,
                binding.includedCheckbox.viewRightTI
            )

            val siViewIds = arrayOf(
                binding.includedCheckbox.viewTopSI,
                binding.includedCheckbox.viewBottomSI,
                binding.includedCheckbox.viewLeftSI,
                binding.includedCheckbox.viewRightSI
            )

            val coViewIds = arrayOf(
                binding.includedCheckbox.viewTopCO,
                binding.includedCheckbox.viewBottomCO,
                binding.includedCheckbox.viewLeftCO,
                binding.includedCheckbox.viewRightCO
            )

            tiViewIds.forEach { it.setBackgroundColor(orangeColor) }
            siViewIds.forEach { it.setBackgroundColor(grayIconsColor) }
            coViewIds.forEach { it.setBackgroundColor(grayIconsColor) }

            binding.includedCheckbox.checkBoxImageTI.setImageResource(R.drawable.checked_box)
            binding.includedCheckbox.checkBoxLabelTI.setTextColor(orangeColor)

            binding.includedCheckbox.checkBoxImageSI.setImageResource(R.drawable.not_checked_box)
            binding.includedCheckbox.checkBoxLabelSI.setTextColor(grayIconsColor)

            binding.includedCheckbox.checkBoxImageCO.setImageResource(R.drawable.not_checked_box)
            binding.includedCheckbox.checkBoxLabelCO.setTextColor(grayIconsColor)

            documentType = "1"
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
                binding.includedCheckbox.viewTopSI,
                binding.includedCheckbox.viewBottomSI,
                binding.includedCheckbox.viewLeftSI,
                binding.includedCheckbox.viewRightSI
            )

            val tiViewIds = arrayOf(
                binding.includedCheckbox.viewTopTI,
                binding.includedCheckbox.viewBottomTI,
                binding.includedCheckbox.viewLeftTI,
                binding.includedCheckbox.viewRightTI
            )

            val coViewIds = arrayOf(
                binding.includedCheckbox.viewTopCO,
                binding.includedCheckbox.viewBottomCO,
                binding.includedCheckbox.viewLeftCO,
                binding.includedCheckbox.viewRightCO
            )

            siViewIds.forEach { it.setBackgroundColor(orangeColor) }
            tiViewIds.forEach { it.setBackgroundColor(grayIconsColor) }
            coViewIds.forEach { it.setBackgroundColor(grayIconsColor) }

            binding.includedCheckbox.checkBoxImageSI.setImageResource(R.drawable.checked_box)
            binding.includedCheckbox.checkBoxLabelSI.setTextColor(orangeColor)

            binding.includedCheckbox.checkBoxImageTI.setImageResource(R.drawable.not_checked_box)
            binding.includedCheckbox.checkBoxLabelTI.setTextColor(grayIconsColor)

            binding.includedCheckbox.checkBoxImageCO.setImageResource(R.drawable.not_checked_box)
            binding.includedCheckbox.checkBoxLabelCO.setTextColor(grayIconsColor)

            documentType = "2"
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
                binding.includedCheckbox.viewTopSI,
                binding.includedCheckbox.viewBottomSI,
                binding.includedCheckbox.viewLeftSI,
                binding.includedCheckbox.viewRightSI
            )

            val tiViewIds = arrayOf(
                binding.includedCheckbox.viewTopTI,
                binding.includedCheckbox.viewBottomTI,
                binding.includedCheckbox.viewLeftTI,
                binding.includedCheckbox.viewRightTI
            )

            val coViewIds = arrayOf(
                binding.includedCheckbox.viewTopCO,
                binding.includedCheckbox.viewBottomCO,
                binding.includedCheckbox.viewLeftCO,
                binding.includedCheckbox.viewRightCO
            )

            coViewIds.forEach { it.setBackgroundColor(orangeColor) }
            siViewIds.forEach { it.setBackgroundColor(grayIconsColor) }
            tiViewIds.forEach { it.setBackgroundColor(grayIconsColor) }

            binding.includedCheckbox.checkBoxImageCO.setImageResource(R.drawable.checked_box)
            binding.includedCheckbox.checkBoxLabelCO.setTextColor(orangeColor)

            binding.includedCheckbox.checkBoxImageSI.setImageResource(R.drawable.not_checked_box)
            binding.includedCheckbox.checkBoxLabelSI.setTextColor(grayIconsColor)

            binding.includedCheckbox.checkBoxImageTI.setImageResource(R.drawable.not_checked_box)
            binding.includedCheckbox.checkBoxLabelTI.setTextColor(grayIconsColor)

            documentType = "3"
        }
    }

    private fun calendarView(text: String) {
        val myCalendar = Calendar.getInstance()

        val datePicker = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            myCalendar[Calendar.YEAR] = year
            myCalendar[Calendar.MONTH] = month
            myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth

            val dateType = if (text == "textBornDate") 0 else 1
            if (dateType == 0 && myCalendar[Calendar.YEAR] > 2008) {
                Toast.makeText(this@CreateWorkerActivity, "El año debe ser máximo 2008", Toast.LENGTH_SHORT).show()
            } else {
                updateLabel(myCalendar, dateType)
            }
        }

        val initialYear = myCalendar[Calendar.YEAR]
        val initialMonth = myCalendar[Calendar.MONTH]
        val initialDayOfMonth = myCalendar[Calendar.DAY_OF_MONTH]

        val datePickerDialog = DatePickerDialog(this@CreateWorkerActivity, datePicker, initialYear, initialMonth, initialDayOfMonth)
        if (text == "textBornDate") {
            datePickerDialog.datePicker.maxDate = getMaximumDateInMillis()
        }

        datePickerDialog.show()
    }

    private fun getMaximumDateInMillis(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(2008, 11, 31, 23, 59, 59)
        return calendar.timeInMillis
    }

    private fun updateLabel(myCalendar: Calendar, direction: Int) {
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.US)

        val textField = if (direction == 0) binding.textBornDate else binding.textJoinDate
        textField.setText(sdf.format(myCalendar.time))
    }

    private fun creatingWorkers() {

        val dni = binding.textDNI.text.toString()
        val name = binding.textName.text.toString()
        val lastname = binding.textLastname.text.toString()
        val dateBirth = binding.textBornDate.text.toString()
        val dateJoin = binding.textJoinDate.text.toString()
        val bloodType = binding.viewBloodType.text.toString()
        val diseases = binding.textIllness.text.toString()
        val allergies = binding.textAllergies.text.toString()
        val phone = binding.textPhone.text.toString()
        val phoneEmergency = binding.textPhoneEmergency.text.toString()
        val photo = binding.imageViewUser
        val gender = binding.viewGender.text.toString()
        val nationality = binding.viewNationality.text.toString()

        val abbreviationGender = when (gender) {
            "Femenino" -> "F"
            "Masculino" -> "M"
            else -> {
                "Otro"
            }
        }

        val regexPhone = Regex(getString(R.string.pattern_principal_number))

        if (!isValidForm(dni, name, lastname)) {
            showErrorAlert(
                this@CreateWorkerActivity,
                getString(R.string.title_missing_parameters),
                getString(R.string.subtitle_missing_parameters)
            )
            return
        }

        if (phone.isEmpty() || !isValidPhoneNumber(regexPhone, phone)) {
            showErrorAlert(
                this@CreateWorkerActivity,
                getString(R.string.title_invalid_phone),
                getString(R.string.subtitle_invalid_phone)
            )
            val messageError = getString(R.string.mandatory)
            toggleTextInputLayoutError(binding.textPhone, messageError)
            return
        }

        if (phoneEmergency.isEmpty() || phoneEmergency == phone) {
            showErrorAlert(
                this@CreateWorkerActivity,
                getString(R.string.title_invalid_second_phone),
                getString(R.string.subtitle_invalid_second_phone)
            )
            val messageError = getString(R.string.mandatory)
            toggleTextInputLayoutError(binding.textPhoneEmergency, messageError)
            return
        }

        if (gender.contentEquals("Sexo")) {
            showErrorAlert(
                this@CreateWorkerActivity,
                getString(R.string.title_missing_parameters),
                getString(R.string.subtitle_missing_gender)
            )
            return
        }

        if (nationality.contentEquals("Nacionalidad")) {
            showErrorAlert(
                this@CreateWorkerActivity,
                getString(R.string.title_missing_parameters),
                getString(R.string.subtitle_missing_nationality)
            )
            return
        }

        if (!isSelectedTI && !isSelectedSI && !isSelectedCO) {
            showErrorAlert(
                this@CreateWorkerActivity,
                getString(R.string.title_missing_parameters),
                getString(R.string.subtitle_missing_area)
            )
            return
        }

        if (dateBirth.isEmpty() || dateJoin.isEmpty()) {
            showErrorAlert(
                this@CreateWorkerActivity,
                getString(R.string.title_missing_parameters),
                getString(R.string.subtitle_missing_dates)
            )
            return
        }


        if (photo.drawable.constantState == ContextCompat.getDrawable(this, R.drawable.ic_baseline_image_24)?.constantState) {
            showErrorAlert(this@CreateWorkerActivity, getString(R.string.title_404_image), getString(R.string.subtitle_image_description))
            return
        }

        showLoading()
        startImageForResult.let {
            imageFile?.let {
                val imageBase = imageFile?.toUri()?.let { it1 -> getBase64ForUriAndPossiblyCrash(it1) }
                val workerUser = WorkersResponse(
                    dni = dni,
                    name = name,
                    lastname = lastname,
                    dateBirth = dateBirth,
                    admissionDate = dateJoin,
                    area = documentType,
                    bloodType = bloodType,
                    diseases = diseases,
                    allergies = allergies,
                    phone = phone,
                    phoneEmergency = phoneEmergency,
                    photo = imageBase,
                    photoFormat = imageFile?.name,
                    gender = abbreviationGender,
                    nationality = nationality
                )
                CoroutineScope(Dispatchers.Default).launch {
                    postWorkersProvider.postWorkers(workerUser)?.enqueue(object : Callback<ResponseHttp> {
                        override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                            handleResponse(response)
                        }

                        override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                            hideLoading()
                            Toast.makeText(this@CreateWorkerActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                        }

                    })
                }
            }
        }
    }

    private fun handleResponse(response: Response<ResponseHttp>) {
        if (response.body()?.code == 200) {
            hideLoading()
            runBlocking {
                deleteCache(this@CreateWorkerActivity)
            }
            clearForm()
            SweetAlertDialog(this@CreateWorkerActivity, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(getString(R.string.title_200_register_worker))
                .show()
        } else {
            hideLoading()
            SweetAlertDialog(this@CreateWorkerActivity, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(getString(R.string.title_error_register))
                .show()
        }
    }

    private fun clearForm() {
        if (imageFile != null) {
            imageFile = null
            binding.imageViewUser.setImageResource(R.drawable.ic_baseline_image_24)
            binding.textDNI.setText("")
            binding.textName.setText("")
            binding.textLastname.setText("")
            binding.textBornDate.setText("")
            binding.textJoinDate.setText("")
            binding.viewBloodType.text = ""
            binding.textIllness.setText("")
            binding.textAllergies.setText("")
            binding.textPhone.setText("")
            binding.textPhoneEmergency.setText("")
            resetStateAreas()
        } else {
            binding.textDNI.setText("")
            binding.textName.setText("")
            binding.textLastname.setText("")
            binding.textBornDate.setText("")
            binding.textJoinDate.setText("")
            binding.viewBloodType.text = ""
            binding.textIllness.setText("")
            binding.textAllergies.setText("")
            binding.textPhone.setText("")
            binding.textPhoneEmergency.setText("")
            resetStateAreas()
        }
    }

    private fun resetStateAreas() {
        isSelectedTI = false
        isSelectedSI = false

        val includedCheckbox = binding.includedCheckbox
        val unselectedColor = ContextCompat.getColor(this, R.color.color_gray_icons)

        val viewsToReset = listOf(
            includedCheckbox.viewTopTI, includedCheckbox.viewBottomTI,
            includedCheckbox.viewLeftTI, includedCheckbox.viewRightTI,
            includedCheckbox.viewTopSI, includedCheckbox.viewBottomSI,
            includedCheckbox.viewLeftSI, includedCheckbox.viewRightSI
        )

        for (view in viewsToReset) {
            view.setBackgroundColor(unselectedColor)
        }

        includedCheckbox.checkBoxImageTI.setImageResource(R.drawable.not_checked_box)
        includedCheckbox.checkBoxImageSI.setImageResource(R.drawable.not_checked_box)

        includedCheckbox.checkBoxLabelTI.setTextColor(unselectedColor)
        includedCheckbox.checkBoxLabelSI.setTextColor(unselectedColor)

        documentType = ""
        Timber.d("DocumentType -> $documentType")
    }

    private fun isValidForm(
        dni: String,
        name: String,
        lastname: String
    ): Boolean {
        if (dni.isBlank() || dni.length < 8) {
            val messageError = getString(R.string.mandatory)
            toggleTextInputLayoutError(binding.textDNI, messageError)
            return false
        }

        if (name.isBlank()) {
            val messageError = getString(R.string.mandatory)
            toggleTextInputLayoutError(binding.textName, messageError)
            return false
        }

        if (lastname.isBlank()) {
            val messageError = getString(R.string.mandatory)
            toggleTextInputLayoutError(binding.textLastname, messageError)
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
            contentResolver.openInputStream(uri)?.use { inputStream ->
                val bytes = inputStream.readBytes()
                Base64.encodeToString(bytes, Base64.DEFAULT)
            } ?: "Error al abrir el archivo"
        } catch (error: IOException) {
            error.printStackTrace()
            "Ha ocurrido un error"
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startNewActivityWithBackAnimation(this@CreateWorkerActivity, ManagementWorkerActivity::class.java)
    }

}