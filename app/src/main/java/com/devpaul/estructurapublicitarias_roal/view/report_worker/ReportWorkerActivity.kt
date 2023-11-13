package com.devpaul.estructurapublicitarias_roal.view.report_worker

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.devpaul.estructurapublicitarias_roal.R
import com.devpaul.estructurapublicitarias_roal.data.models.entity.PrincipalListWorker
import com.devpaul.estructurapublicitarias_roal.databinding.ActivityReportWorkerBinding
import com.devpaul.estructurapublicitarias_roal.domain.usecases.reportWorker.WorkerReportResult
import com.devpaul.estructurapublicitarias_roal.domain.utils.*
import com.devpaul.estructurapublicitarias_roal.view.HomeActivity
import com.devpaul.estructurapublicitarias_roal.view.base.BaseActivity
import timber.log.Timber

class ReportWorkerActivity : BaseActivity() {

    lateinit var binding: ActivityReportWorkerBinding
    private lateinit var viewModel: ReportWorkerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportWorkerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbarStyle(
            this@ReportWorkerActivity,
            binding.include.toolbar,
            REPORT_SECTION,
            true,
            HomeActivity::class.java
        )

        viewModel =
            ViewModelProvider(this, ViewModelFactory(this, ReportWorkerViewModel::class.java))[ReportWorkerViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.reportWorkerResult.observe(this) { result ->
            handleManagementWorkerResult(result)
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
        viewModel.validateAllWorkers()
    }

    private fun handleManagementWorkerResult(result: WorkerReportResult) {
        when (result) {
            is WorkerReportResult.Success -> {
                showAllWorker(result.data)
            }

            is WorkerReportResult.Error -> {

            }

            is WorkerReportResult.ValidationError -> {
                Toast.makeText(this, result.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showAllWorker(data: List<PrincipalListWorker>) {

        var selectedDni: String

        binding.allWorkers.setOnClickListener {

            val dialog = Dialog(this@ReportWorkerActivity)
            dialog.setContentView(R.layout.dialog_searchable_spinner)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()

            val textTitle: TextView = dialog.findViewById(R.id.titleSearchView)
            val editText: EditText = dialog.findViewById(R.id.edit_text)
            val listView: ListView = dialog.findViewById(R.id.list_view)

            val workerNamesList = data.map { "${it.name} ${it.lastname}" }
            val adapter = ArrayAdapter(this@ReportWorkerActivity, android.R.layout.simple_list_item_1, workerNamesList)

            listView.adapter = adapter
            textTitle.text = "Trabajadores"

            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    adapter.filter.filter(charSequence)
                }

                override fun afterTextChanged(editable: Editable) {}
            })

            listView.setOnItemClickListener { _, _, i, _ ->
                val selectedWorker = data[i]
                selectedDni = selectedWorker.document.toString()
                binding.allWorkers.text = adapter.getItem(i)
                dialog.dismiss()
                Timber.d("DocumentSelected $selectedDni ${selectedWorker.name}")
                //  viewModel.getInformation(selectedWorker.document)
            }
        }
    }

}