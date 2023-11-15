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
import com.devpaul.estructurapublicitarias_roal.databinding.ActivityReportWorkerBinding
import com.devpaul.estructurapublicitarias_roal.domain.usecases.reportWorker.WorkerReportResult
import com.devpaul.estructurapublicitarias_roal.domain.utils.*
import com.devpaul.estructurapublicitarias_roal.view.HomeActivity
import com.devpaul.estructurapublicitarias_roal.view.base.BaseActivity
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import timber.log.Timber
import java.util.ArrayList

class ReportWorkerActivity : BaseActivity() {

    lateinit var binding: ActivityReportWorkerBinding
    private lateinit var viewModel: ReportWorkerViewModel
    private var isPressed = false
    var pieChart: PieChart? = null

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
        initPieChart()
    }

    private fun initPieChart() {
        pieChart?.setUsePercentValues(true)
        pieChart?.description?.text = ""
        //hollow pie chart
        pieChart?.isDrawHoleEnabled = false
        pieChart?.setTouchEnabled(false)
        pieChart?.setDrawEntryLabels(false)
        //adding padding
        pieChart?.setExtraOffsets(20f, 0f, 20f, 20f)
        pieChart?.setUsePercentValues(true)
        pieChart?.isRotationEnabled = false
        pieChart?.setDrawEntryLabels(false)
        pieChart?.legend?.orientation = Legend.LegendOrientation.VERTICAL
        pieChart?.legend?.isWordWrapEnabled = true

    }

    private fun handleManagementWorkerResult(result: WorkerReportResult) {
        when (result) {
            is WorkerReportResult.Success -> {
                showAllWorker(result.data)
            }

            is WorkerReportResult.Error -> {
                showCustomDialogErrorSingleton(this,
                    TITLE_ERROR_MS_ALL_WORKERS,
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
        }
    }

    private fun showAllWorker(data: List<PrincipalListWorker>) {
        var selectedDni: String
        optionsReportUI()
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

    private fun optionsReportUI() {

        binding.horizontalScrollViewReport.visibility = View.VISIBLE

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

        btnUno.setBackgroundColor(grayColor)

        applyButtonSelectionLogic(cardViewButtons, grayColor, whiteColor) { button ->
            cardViewButtons.filter { it != button }.forEach { it.setBackgroundColor(whiteColor) }

            when (button) {
                btnUno -> pieChartest()
                btnDos -> ""
                btnTres -> ""
                btnCua -> ""
                else -> {

                }
            }
        }
    }

    private fun pieChartest() {
        pieChart?.setUsePercentValues(true)
        val dataEntries = ArrayList<PieEntry>()
        dataEntries.add(PieEntry(2.5.toFloat(), "Correctas"))
        dataEntries.add(PieEntry(3.5.toFloat(), "Incorrectas"))

        val colors: ArrayList<Int> = ArrayList()
        colors.add(Color.parseColor("#4DD0E1"))
        colors.add(Color.parseColor("#FFF176"))

        val dataSet = PieDataSet(dataEntries, "")
        val datachart = PieData(dataSet)
        datachart.setValueTextSize(15f)

        // In Percentage
        datachart.setValueFormatter(PercentFormatter())
        dataSet.sliceSpace = 3f
        dataSet.colors = colors
        pieChart?.data = datachart
        datachart.setValueTextSize(15f)
        pieChart?.setExtraOffsets(5f, 10f, 5f, 5f)
        pieChart?.animateY(1400, Easing.EaseInOutQuad)

        //create hole in center
        pieChart?.holeRadius = 58f
        pieChart?.transparentCircleRadius = 61f
        pieChart?.isDrawHoleEnabled = true
        pieChart?.setHoleColor(Color.WHITE)


        //add text in cente
        pieChart?.setDrawCenterText(true)
        pieChart?.centerText = "Estadística"
        pieChart?.setCenterTextSize(15f)
        pieChart?.invalidate()
    }

}