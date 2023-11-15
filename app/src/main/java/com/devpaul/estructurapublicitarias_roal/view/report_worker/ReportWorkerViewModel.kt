package com.devpaul.estructurapublicitarias_roal.view.report_worker

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devpaul.estructurapublicitarias_roal.data.models.entity.ValidationEPPReportEntity
import com.devpaul.estructurapublicitarias_roal.data.repository.WorkersReportRepository
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.usecases.reportWorker.WorkerReportResult
import com.devpaul.estructurapublicitarias_roal.domain.usecases.reportWorker.WorkerReportUseCase
import com.devpaul.estructurapublicitarias_roal.domain.utils.RoundedPercentageFormatter
import com.devpaul.estructurapublicitarias_roal.domain.utils.SingletonError
import com.devpaul.estructurapublicitarias_roal.view.base.BaseViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.launch

class ReportWorkerViewModel(context: Context) : BaseViewModel() {

    private val _reportWorkerResult = MutableLiveData<WorkerReportResult>()

    val reportWorkerResult: LiveData<WorkerReportResult> = _reportWorkerResult

    val questionReport = MutableLiveData("")

    val titleDescriptionText = MutableLiveData("")

    val descriptionList = MutableLiveData("")

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

    fun validateFirstButtonReport(pieChart: PieChart?, data: ValidationEPPReportEntity?, linearDataReport: LinearLayout) {

        //Mostrando valores como porcentajes
        pieChart?.setUsePercentValues(true)

        linearDataReport.visibility = View.VISIBLE

        val dataEntries = data?.dataEntriesEPP?.pieDescription
        val pieEntries = data?.dataEntriesEPP?.pieEntries ?: emptyList()
        val additionalDataList = data?.additionalData ?: emptyList()

        val descriptionTexts = additionalDataList.mapIndexed { index, additionalData ->
            "${additionalData.key}: ${additionalData.value}" + if (index < additionalDataList.size - 1) "\n\n" else ""
        }

        titleDescriptionText.value = dataEntries?.bottomText
        descriptionList.value = descriptionTexts.joinToString(separator = "")

        val colors: List<Int> = pieEntries.map { entry ->
            Color.parseColor(entry.colorEntry)
        }

        val dataSet = PieDataSet(
            pieEntries.map { entry ->
                PieEntry(
                    entry.valueEntry.toFloat(),
                    entry.labelEntry
                )
            },
            ""
        )

        val dataChart = PieData(dataSet)

        // In Percentage (usando el nuevo formateador redondeado)
        dataChart.setValueFormatter(RoundedPercentageFormatter())

        // Espacio entre las barras
        dataSet.sliceSpace = 10f
        dataSet.colors = colors
        pieChart?.data = dataChart
        dataChart.setValueTextSize(16f)
        pieChart?.setExtraOffsets(6f, 6f, 6f, 6f)
        pieChart?.animateY(1400, Easing.EaseInOutQuad)

        // Grosor de los colores
        pieChart?.holeRadius = 56f
        pieChart?.transparentCircleRadius = 60f
        //Habilita la visualización del agujero en el centro del gráfico
        pieChart?.isDrawHoleEnabled = true
        //Rotacion del PieChar
        pieChart?.isRotationEnabled = true
        pieChart?.setHoleColor(Color.WHITE)

        // Texto en el centro
        pieChart?.setDrawCenterText(true)
        pieChart?.centerText = dataEntries?.centerText ?: ""
        pieChart?.setCenterTextSize(14f)

        //Pregunta como titulo, identificador principal
        questionReport.value = dataEntries?.topText ?: ""

        //Deshabilita la visualización de etiquetas de entrada en el gráfico
        pieChart?.setDrawEntryLabels(false)

        // Configurar el tamaño del texto de la leyenda
        val legend = pieChart?.legend
        val description = pieChart?.description

        legend?.textSize = 14f
        legend?.orientation = Legend.LegendOrientation.VERTICAL
        legend?.isWordWrapEnabled = true
        description?.text = ""

        pieChart?.invalidate()
    }


}