package com.devpaul.estructurapublicitarias_roal.domain.usecases.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory<T : ViewModel>(private val context: Context, private val viewModelClass: Class<T>) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(viewModelClass)) {
            return viewModelClass.getConstructor(Context::class.java).newInstance(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
