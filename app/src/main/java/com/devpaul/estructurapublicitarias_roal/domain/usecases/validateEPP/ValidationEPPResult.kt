package com.devpaul.estructurapublicitarias_roal.domain.usecases.validateEPP

import com.devpaul.estructurapublicitarias_roal.data.models.entity.ValidationEPP

sealed class ValidationEPPResult {
    data class Success(val data: ValidationEPP) : ValidationEPPResult()
    data class Error(val code: Int?, val title: String?, val subTitle: String?) : ValidationEPPResult()
    data class ExceptionError(val exception: Exception) : ValidationEPPResult()
    data object MissingImage : ValidationEPPResult()
    data object DefaultColorsComponents : ValidationEPPResult()
}