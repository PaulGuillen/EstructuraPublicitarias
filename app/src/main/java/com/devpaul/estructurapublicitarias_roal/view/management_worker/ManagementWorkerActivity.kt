package com.devpaul.estructurapublicitarias_roal.view.management_worker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions.centerCropTransform
import com.devpaul.estructurapublicitarias_roal.R
import com.devpaul.estructurapublicitarias_roal.databinding.ActivityManagementWorkerBinding
import com.devpaul.estructurapublicitarias_roal.domain.usecases.mangementWorker.ManagementWorkerResult
import com.devpaul.estructurapublicitarias_roal.domain.utils.*
import com.devpaul.estructurapublicitarias_roal.view.HomeActivity
import com.devpaul.estructurapublicitarias_roal.view.base.BaseActivity

@SuppressLint("SourceLockedOrientationActivity")
class ManagementWorkerActivity : BaseActivity() {

    lateinit var binding: ActivityManagementWorkerBinding
    private lateinit var viewModel: ManagementWorkerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagementWorkerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbarStyle(
            this@ManagementWorkerActivity,
            binding.include.toolbar,
            MAINTENANCE_WORKER,
            true,
            HomeActivity::class.java
        )

        viewModel =
            ViewModelProvider(this, ViewModelFactory(this, ManagementWorkerViewModel::class.java))[ManagementWorkerViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.managementWorkerResult.observe(this) { result ->
            handleManagementWorkerResult(result)
        }

        viewModel.showLoadingDialog.observe(this) { showLoading ->
            if (showLoading) {
                showLoading()
            } else {
                hideLoading()
            }
        }

        searchWorkers()
    }


    private fun handleManagementWorkerResult(result: ManagementWorkerResult) {
        when (result) {
            is ManagementWorkerResult.Success -> {

                result.data.photo.let {
                    Glide.with(this@ManagementWorkerActivity)
                        .load(result.data.photo)
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

                binding.textDataNeed.visibility = View.GONE
                binding.linearLayoutNoDataFound.visibility = View.GONE
                binding.cardViewWorker.cardViewPrincipal.visibility = View.VISIBLE
            }

            is ManagementWorkerResult.CreateWorker -> {
                startNewActivityWithAnimation(this@ManagementWorkerActivity, CreateWorkerActivity::class.java)
            }

            is ManagementWorkerResult.UpdateWorker -> {
                val extras = Bundle()
                val dni = result.dni
                extras.putString("dni", dni)
                startNewActivityWithAnimation(this@ManagementWorkerActivity, UpdateWorkerActivity::class.java, extras, true)
            }

            is ManagementWorkerResult.DeleteWorker -> {
                deleteWorker()
            }

            is ManagementWorkerResult.SuccessWorkerDeleted -> {
                SweetAlertDialog(this@ManagementWorkerActivity, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(getString(R.string.successful_delete_data)).show()
                binding.textDataNeed.visibility = View.VISIBLE
                binding.cardViewWorker.cardViewPrincipal.visibility = View.GONE
            }

            is ManagementWorkerResult.Error -> {
                dismissData(result.code)
            }

            is ManagementWorkerResult.ValidationError -> {
                Toast.makeText(this, MESSAGE_DATA_NOT_VALID, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun searchWorkers() {

        binding.searchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No se necesita implementación en este caso
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val inputLength = s?.length ?: 0
                if (inputLength != 0 && (inputLength < 8 || inputLength > 20)) {
                    binding.searchBox.error = getString(R.string.text_dni_or_cex_not_allowed)
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
                    viewModel.validateWorkerByDNI()
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
                viewModel.validateDeleteWorker()
                it.dismiss()
            }.show()
    }


    private fun dismissData(codState: Int?) {
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

    private fun performSearch() {
        binding.searchBox.clearFocus()
        val `in`: InputMethodManager = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE)
                as InputMethodManager
        `in`.hideSoftInputFromWindow(binding.searchBox.windowToken, 0)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startNewActivityWithBackAnimation(this@ManagementWorkerActivity, HomeActivity::class.java)
    }
}