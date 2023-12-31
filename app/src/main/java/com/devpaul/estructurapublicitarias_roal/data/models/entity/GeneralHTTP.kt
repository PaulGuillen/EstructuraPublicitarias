package com.devpaul.estructurapublicitarias_roal.data.models.entity

import com.google.gson.JsonObject

data class GeneralHTTP(
    var isSuccess: String? = null,
    var message: String? = null,
    var code: Int? = null,
    var data: JsonObject? = null,
    var error: String? = null
)