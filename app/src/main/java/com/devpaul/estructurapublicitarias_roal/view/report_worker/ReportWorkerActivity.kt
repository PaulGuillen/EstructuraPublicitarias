package com.devpaul.estructurapublicitarias_roal.view.report_worker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.devpaul.estructurapublicitarias_roal.databinding.ActivityReportWorkerBinding
import com.devpaul.estructurapublicitarias_roal.domain.utils.*
import com.devpaul.estructurapublicitarias_roal.view.HomeActivity

class ReportWorkerActivity : AppCompatActivity() {

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

    }
}