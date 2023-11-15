package com.devpaul.estructurapublicitarias_roal.view.report_worker

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devpaul.estructurapublicitarias_roal.data.models.entity.WorkerReportByUser
import com.devpaul.estructurapublicitarias_roal.data.repository.WorkersReportRepository
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.usecases.reportWorker.WorkerReportResult
import com.devpaul.estructurapublicitarias_roal.domain.usecases.reportWorker.WorkerReportUseCase
import com.devpaul.estructurapublicitarias_roal.domain.utils.SingletonError
import com.devpaul.estructurapublicitarias_roal.view.base.BaseViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import kotlinx.coroutines.launch
import java.util.ArrayList

class ReportWorkerViewModel(context: Context) : BaseViewModel() {

    private val _reportWorkerResult = MutableLiveData<WorkerReportResult>()

    val reportWorkerResult: LiveData<WorkerReportResult> = _reportWorkerResult

    init {
        validateAllWorkers()
    }

    private fun validateAllWorkers() {
        viewModelScope.launch {
            callServiceAllWorkers()
        }
    }

    fun validateDataByWorker(document: String) {
        viewModelScope.launch {
            callServiceGetInformation(document)
        }
    }

    private suspend fun callServiceAllWorkers() {

        _showLoadingDialog.value = true
        try {
            val workerReportRepository = WorkersReportRepository()
            val managementWorkerReportUseCase = WorkerReportUseCase(workerReportRepository)
            when (val getWorker = managementWorkerReportUseCase.allWorkers()) {
                is CustomResult.OnSuccess -> {
                    val data = getWorker.data
                    _reportWorkerResult.value = WorkerReportResult.Success(data)
                }

                is CustomResult.OnError -> {
                    val error = getWorker.error
                    _reportWorkerResult.value = WorkerReportResult.Error(
                        error.code ?: SingletonError.code,
                        error.title ?: SingletonError.title,
                        error.subtitle ?: SingletonError.subTitle
                    )
                }
            }
        } catch (e: Exception) {
            _reportWorkerResult.value = WorkerReportResult.ValidationError(e)
        } finally {
            _showLoadingDialog.value = false
        }

    }

    private suspend fun callServiceGetInformation(document: String) {

        _showLoadingDialog.value = true
        try {
            val workerReportRepository = WorkersReportRepository()
            val managementWorkerReportUseCase = WorkerReportUseCase(workerReportRepository)
            when (val getWorker = managementWorkerReportUseCase.reportByWorker(document)) {
                is CustomResult.OnSuccess -> {
                    val data = getWorker.data
                    _reportWorkerResult.value = WorkerReportResult.SuccessDataReportByWorker(data)
                }

                is CustomResult.OnError -> {
                    val error = getWorker.error
                    _reportWorkerResult.value = WorkerReportResult.Error(
                        error.code ?: SingletonError.code,
                        error.title ?: SingletonError.title,
                        error.subtitle ?: SingletonError.subTitle
                    )
                }
            }
        } catch (e: Exception) {
            _reportWorkerResult.value = WorkerReportResult.ValidationError(e)
        } finally {
            _showLoadingDialog.value = false
        }

    }

    fun initPieChart(pieChart: PieChart?) {
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

    fun validateFirstButtonReport(pieChart: PieChart?, data: WorkerReportByUser) {

        pieChart?.visibility = View.VISIBLE
        pieChart?.setUsePercentValues(true)

        val pieEntries = data.validationEPP?.dataEntriesEPP?.pieEntries ?: emptyList()

        val colors: List<Int> = pieEntries.map { entry ->
            Color.parseColor(entry.colorEntry)
        }

        val dataSet = PieDataSet(
            pieEntries.map { entry ->
                PieEntry(
                    entry.valueEntry?.toFloat() ?: 0.0.toFloat(),
                    entry.labelEntry
                )
            },
            ""
        )

        val dataChart = PieData(dataSet)
        dataChart.setValueTextSize(15f)

        // In Percentage
        dataChart.setValueFormatter(PercentFormatter(pieChart))
        dataSet.sliceSpace = 10f
        dataSet.colors = colors

        pieChart?.data = dataChart
        dataChart.setValueTextSize(14f)
        pieChart?.setExtraOffsets(5f, 5f, 5f, 5f)
        pieChart?.animateY(1400, Easing.EaseInOutQuad)

        // create hole in center
        // Grosor de los colores
        pieChart?.holeRadius = 50f
        pieChart?.transparentCircleRadius = 61f
        pieChart?.isDrawHoleEnabled = true
        pieChart?.setHoleColor(Color.WHITE)

        // add text in center
        pieChart?.setDrawCenterText(true)
        pieChart?.centerText = data.validationEPP?.additionalData?.titleReport ?: ""
        pieChart?.setCenterTextSize(14f)
        pieChart?.invalidate()
    }


}