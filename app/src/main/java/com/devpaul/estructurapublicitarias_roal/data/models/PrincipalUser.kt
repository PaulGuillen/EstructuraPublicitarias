package com.devpaul.estructurapublicitarias_roal.data.models

import com.google.gson.JsonObject

data class PrincipalUser(
    var isSuccess: String? = null,
    var message: String? = null,
    var code: Int? = null,
    var data: JsonObject? = null,
    var error: String? = null
)