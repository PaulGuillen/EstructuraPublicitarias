package com.devpaul.estructurapublicitarias_roal.view.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch

abstract class BaseViewModel() : ViewModel() {

    fun launch(block: suspend CoroutineScope.() -> Unit){
        viewModelScope.launch(context = NonCancellable, block = block)
    }

    val _showLoadingDialog = MutableLiveData<Boolean>()

    val showLoadingDialog: LiveData<Boolean>
        get() = _showLoadingDialog

}