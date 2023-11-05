package com.devpaul.estructurapublicitarias_roal.domain.usecases.forgotPassword

import com.devpaul.estructurapublicitarias_roal.data.models.PrincipalUser

sealed class ForgotPasswordResult {
    data class Success(val data: PrincipalUser) : ForgotPasswordResult()
    data class Error(val code: Int?, val title: String?, val subTitle: String?) : ForgotPasswordResult()
    data object ValidationError : ForgotPasswordResult()
    data object ReSendEmailSuccess : ForgotPasswordResult()
}
