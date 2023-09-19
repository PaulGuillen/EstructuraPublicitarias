package com.devpaul.estructurapublicitarias_roal.view.management_worker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions.centerCropTransform
import com.devpaul.estructurapublicitarias_roal.R
import com.devpaul.estructurapublicitarias_roal.data.models.Worker
import com.devpaul.estructurapublicitarias_roal.data.models.response.ResponseHttp
import com.devpaul.estructurapublicitarias_roal.data.repository.WorkersRepository
import com.devpaul.estructurapublicitarias_roal.databinding.ActivityManagementWorkerBinding
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.usecases.WorkerUseCase
import com.devpaul.estructurapublicitarias_roal.domain.utils.SingletonError
import com.devpaul.estructurapublicitarias_roal.domain.utils.toolbarStyle
import com.devpaul.estructurapublicitarias_roal.providers.WorkersProvider
import com.devpaul.estructurapublicitarias_roal.view.base.BaseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

@SuppressLint("SourceLockedOrientationActivity")
class ManagementWorkerActivity : BaseActivity() {

    lateinit var binding: ActivityManagementWorkerBinding
    private var workersProvider = WorkersProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagementWorkerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbarStyle(this@ManagementWorkerActivity, binding.include.toolbar, "Mantenimiento de Trabajadores")
        binding.fabWorkerCreate.setOnClickListener { goToWorkerCreate() }
        binding.cardViewWorker.btnDeleteWorker.setOnClickListener { deleteWorker() }
        binding.cardViewWorker.btnUpdateWorker.setOnClickListener { goToUpdateWorkers() }
        searchWorkers()
    }

    private fun searchWorkers() {

        binding.searchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No se necesita implementación en este caso
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val inputLength = s?.length ?: 0
                if (inputLength != 0 && (inputLength < 8 || inputLength > 20)) {
                    binding.searchBox.error = "DNI o CEX no permitido"
                } else {
                    binding.searchBox.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // No se necesita implementación en este caso
            }
        })

        binding.searchBox.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (binding.searchBox.error == null) {
                    getWorkerService()
                    performSearch()
                }
                true
            } else {
                false
            }
        }
    }

    private fun deleteWorker() {
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText(getString(R.string.title_delete_data))
            .setCancelText("No").setCancelClickListener { obj: SweetAlertDialog -> obj.dismiss() }
            .setConfirmText("Si")
            .setConfirmClickListener {
                deleteWorkers()
                it.dismiss()
            }.show()
    }

    private fun deleteWorkers() {
        val actualIdentification = binding.cardViewWorker.textViewDNI.text.toString()
        showLoading()
        workersProvider.deleteWorker(actualIdentification)?.enqueue(object : Callback<ResponseHttp> {
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                SweetAlertDialog(this@ManagementWorkerActivity, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(getString(R.string.successful_delete_data)).show()
                hideLoading()
                binding.textDataNeed.visibility = View.VISIBLE
                binding.cardViewWorker.cardViewPrincipal.visibility = View.GONE

            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                hideLoading()
                Toast.makeText(this@ManagementWorkerActivity, "Error : $t", Toast.LENGTH_LONG).show()
            }

        })

    }

    private fun getWorkerService() {
        showLoading()
        CoroutineScope(Dispatchers.IO).launch {
            val dni = binding.searchBox.text.toString()
            try {
                val workersRepository = WorkersRepository()
                val workerUseCase = WorkerUseCase(this@ManagementWorkerActivity, workersRepository)
                val serviceWorker = workerUseCase.getWorkers(dni)
                withContext(Dispatchers.Main) {
                    hideLoading()
                    when (serviceWorker) {
                        is CustomResult.OnSuccess -> {
                            val data = serviceWorker.data
                            showData(data)
                        }

                        is CustomResult.OnError -> {
                            val codeState = SingletonError.code
                            dismissData(codeState ?: 408)
                        }
                    }
                }

            } catch (e: Exception) {
                0
                Timber.d("Response $e")
            }

        }
    }

    private fun dismissData(codState: Int) {
        if (codState == 500 || codState == 408) {
            binding.cardViewWorker.cardViewPrincipal.visibility = View.GONE
            binding.textDataNeed.visibility = View.GONE
            binding.linearLayoutNoDataFound.visibility = View.VISIBLE
            binding.textDataManagementErrors.text = getString(R.string.inforamcion_error_de_servicio)
            binding.textDataManagementErrors.setCompoundDrawablesWithIntrinsicBounds(
                0, R.drawable.service_error, 0, 0
            )
        } else {
            binding.cardViewWorker.cardViewPrincipal.visibility = View.GONE
            binding.textDataNeed.visibility = View.GONE
            binding.linearLayoutNoDataFound.visibility = View.VISIBLE
        }
    }

    private fun showData(worker: Worker) {

        binding.textDataNeed.visibility = View.GONE
        binding.linearLayoutNoDataFound.visibility = View.GONE
        binding.cardViewWorker.cardViewPrincipal.visibility = View.VISIBLE

        worker.photo.let {
            Glide.with(this@ManagementWorkerActivity)
                .load(worker.photo)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .apply(
                    centerCropTransform()
                        .placeholder(R.drawable.ic_baseline_supervised_user_circle_24)
                        .error(R.drawable.ic_baseline_supervised_user_circle_24)
                        .priority(Priority.HIGH)
                )
                .into(binding.cardViewWorker.imageWorker)

        }
        binding.cardViewWorker.textViewDNI.text = worker.dni
        val fullName = getString(R.string.full_name, worker.name, worker.lastname)
        binding.cardViewWorker.textViewNomCompleto.text = fullName
        binding.cardViewWorker.textArea.text = worker.area
        binding.cardViewWorker.textPhonePrincipal.text = worker.phone
        binding.cardViewWorker.textdateJoin.text = worker.dateJoin

        resetStatus()
    }

    private fun goToWorkerCreate() {
        val i = Intent(this, CreateWorkerActivity::class.java)
        startActivity(i)
    }

    private fun goToUpdateWorkers() {
        val actualIdentification = binding.cardViewWorker.textViewDNI.text.toString()
        val i = Intent(this, UpdateWorkerActivity::class.java)
        i.putExtra("dni", actualIdentification)
        startActivity(i)
    }

    private fun resetStatus() {
        binding.searchBox.setText("")
    }

    private fun performSearch() {
        binding.searchBox.clearFocus()
        val `in`: InputMethodManager = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE)
                as InputMethodManager
        `in`.hideSoftInputFromWindow(binding.searchBox.windowToken, 0)
    }

}