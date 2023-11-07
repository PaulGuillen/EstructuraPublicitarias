package com.devpaul.estructurapublicitarias_roal.view

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.devpaul.estructurapublicitarias_roal.data.models.entity.Options
import com.devpaul.estructurapublicitarias_roal.data.repository.WorkersRepository
import com.devpaul.estructurapublicitarias_roal.databinding.ActivityHomeBinding
import com.devpaul.estructurapublicitarias_roal.domain.adapter.OptionsAdapter
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.usecases.WorkerUseCase
import com.devpaul.estructurapublicitarias_roal.domain.utils.SharedPref
import com.devpaul.estructurapublicitarias_roal.domain.utils.SingletonError
import com.devpaul.estructurapublicitarias_roal.domain.utils.showCustomDialogErrorSingleton
import com.devpaul.estructurapublicitarias_roal.domain.utils.startNewActivityWithAnimation
import com.devpaul.estructurapublicitarias_roal.view.base.BaseActivity
import com.devpaul.estructurapublicitarias_roal.view.emergency.EmergencyActivity
import com.devpaul.estructurapublicitarias_roal.view.settings.SettingsActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@SuppressLint("SourceLockedOrientationActivity")
class HomeActivity : BaseActivity() {

    lateinit var binding: ActivityHomeBinding
    private var adapter: OptionsAdapter? = null
    private var isSpeedDialOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val recyclerView = binding.recyclerviewOptions
        recyclerView.layoutManager = GridLayoutManager(this, 1)

        getCategories()

        binding.listOptions.setOnClickListener {
            toggleSpeedDial()
        }

        binding.emergencyFab.setOnClickListener {
            startNewActivityWithAnimation(this@HomeActivity, EmergencyActivity::class.java)
        }

        binding.settingsFab.setOnClickListener {
            startNewActivityWithAnimation(this@HomeActivity, SettingsActivity::class.java)
        }
    }

    private fun toggleSpeedDial() {
        isSpeedDialOpen = !isSpeedDialOpen

        if (isSpeedDialOpen) {
            binding.emergencyFab.show()
            binding.settingsFab.show()
        } else {
            binding.emergencyFab.hide()
            binding.settingsFab.hide()
        }
    }

    private fun getCategories() {
        val prefs = SharedPref(applicationContext)
        val optionsCategory = prefs.getJsonListOptions("OptionsCategory")
//        if (!optionsCategory.isNullOrEmpty()) {
//            displayCategories(optionsCategory)
//        } else {
//            callServiceGetCategories()
//        }
        callServiceGetCategories()
    }

    private fun callServiceGetCategories() {
        val prefs = SharedPref(applicationContext)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val workersRepository = WorkersRepository()
                val workerUseCase = WorkerUseCase(this@HomeActivity, workersRepository)
                val serviceWorker = workerUseCase.getOptions()
                withContext(Dispatchers.Main) {
                    when (serviceWorker) {
                        is CustomResult.OnSuccess -> {
                            val optionsCategories = serviceWorker.data
                            prefs.saveJsonListOptions("OptionsCategory", optionsCategories)
                            displayCategories(optionsCategories)
                        }

                        is CustomResult.OnError -> {
                            handleServiceError()
                        }
                    }
                }
            } catch (e: Exception) {
                Timber.d("Response $e")
            }
        }
    }

    private fun displayCategories(categories: List<Options>) {
        binding.shimmerFrameLayout.visibility = View.GONE
        binding.recyclerviewOptions.visibility = View.VISIBLE
        adapter = OptionsAdapter(this@HomeActivity, categories)
        binding.recyclerviewOptions.adapter = adapter
    }

    private fun handleServiceError() {
        binding.shimmerFrameLayout.visibility = View.GONE
        binding.recyclerviewOptions.visibility = View.VISIBLE
        val codeState = SingletonError.code
        val title = SingletonError.title
        val subTitle = SingletonError.subTitle
        showCustomDialogErrorSingleton(this@HomeActivity, title, subTitle, codeState, "Aceptar", onClickListener = {})
    }

    override fun onResume() {
        super.onResume()
        binding.shimmerFrameLayout.startShimmerAnimation()
    }

    override fun onPause() {
        super.onPause()
        binding.shimmerFrameLayout.startShimmerAnimation()
    }

}
