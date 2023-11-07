package com.devpaul.estructurapublicitarias_roal.view.emergency

import android.content.Context
import androidx.lifecycle.ViewModel
import com.devpaul.estructurapublicitarias_roal.domain.utils.SharedPref

class EmergencyViewModel(context: Context) : ViewModel() {

    private val prefs = SharedPref(context)

}