package com.devpaul.estructurapublicitarias_roal.view.report_worker

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.devpaul.estructurapublicitarias_roal.R
import com.devpaul.estructurapublicitarias_roal.data.models.entity.PrincipalListWorker
import com.devpaul.estructurapublicitarias_roal.data.models.entity.WorkerReportByUser
import com.devpaul.estructurapublicitarias_roal.databinding.ActivityReportWorkerBinding
import com.devpaul.estructurapublicitarias_roal.domain.usecases.reportWorker.WorkerReportResult
import com.devpaul.estructurapublicitarias_roal.domain.utils.*
import com.devpaul.estructurapublicitarias_roal.view.HomeActivity
import com.devpaul.estructurapublicitarias_roal.view.base.BaseActivity
import com.github.mikephil.charting.charts.PieChart

class ReportWorkerActivity : BaseActivity() {

    lateinit var binding: ActivityReportWorkerBinding
    private lateinit var viewModel: ReportWorkerViewModel
    private var isPressed = false
    private var pieChart: PieChart? = null

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

        pieChart = binding.pieChart
    }

    private fun handleManagementWorkerResult(result: WorkerReportResult) {
        when (result) {
            is WorkerReportResult.Success -> {
                showAllWorker(result.data)
            }

            is WorkerReportResult.Error -> {
                showCustomDialogErrorSingleton(this,
                    result.title,
                    result.subTitle,
                    result.code,
                    getString(R.string.dialog_singleton_text_button_accept),
                    onClickListener = {
                        startNewActivityWithBackAnimation(this, HomeActivity::class.java)
                    })
            }

            is WorkerReportResult.ValidationError -> {
                Toast.makeText(this, result.exception.toString(), Toast.LENGTH_SHORT).show()
            }

            is WorkerReportResult.SuccessDataReportByWorker -> {
                optionsReportUI(result.data)
            }
        }
    }

    private fun showAllWorker(data: List<PrincipalListWorker>) {
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
                val selectedDni = selectedWorker.document.toString()
                binding.allWorkers.text = adapter.getItem(i)
                viewModel.validateDataByWorker(selectedDni)
                dialog.dismiss()

            }
        }

    }

    private fun optionsReportUI(data: WorkerReportByUser) {

        binding.horizontalScrollViewReport.visibility = View.VISIBLE

        viewModel.initPieChart(pieChart)
        isPressed = true

        val grayColor = ContextCompat.getColor(this, R.color.mid_gray_card)
        val whiteColor = ContextCompat.getColor(this, R.color.white)

        val btnUno = binding.btnUno
        val btnDos = binding.btnDos
        val btnTres = binding.btnTres
        val btnCua = binding.btnCuatro

        val cardViewButtons = listOf(
            btnUno, btnDos, btnTres, btnCua
        )

        cardViewButtons.forEach { it.setBackgroundColor(whiteColor) }

        applyButtonSelectionLogic(cardViewButtons, grayColor, whiteColor) { button ->
            cardViewButtons.filter { it != button }.forEach { it.setBackgroundColor(whiteColor) }

            when (button) {
                btnUno -> viewModel.validateFirstButtonReport(pieChart, data)
                btnDos -> ""
                btnTres -> ""
                btnCua -> ""
                else -> {

                }
            }
        }
    }


}