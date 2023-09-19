package com.devpaul.estructurapublicitarias_roal.domain.utils

import com.devpaul.estructurapublicitarias_roal.BuildConfig.DEBUG
import timber.log.Timber

object TimberFactory {
    fun setupOnDebug() {
        Timber.uprootAll()
        if (DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}