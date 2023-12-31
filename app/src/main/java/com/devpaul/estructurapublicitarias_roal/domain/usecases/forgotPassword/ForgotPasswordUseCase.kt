package com.devpaul.estructurapublicitarias_roal.domain.usecases.forgotPassword

import com.devpaul.estructurapublicitarias_roal.data.models.entity.GeneralHTTP
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.interfaces.repository.LoginRepositoryNetwork
import com.devpaul.estructurapublicitarias_roal.data.models.response.MainUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ForgotPasswordUseCase(private val loginRepositoryNetwork: LoginRepositoryNetwork) {

    suspend fun forgotPassword(mainUser: MainUser): CustomResult<GeneralHTTP> = withContext(Dispatchers.IO) {
        val forgotPassword = loginRepositoryNetwork.forgotPassword(mainUser)
        when (forgotPassword) {
            is CustomResult.OnSuccess -> {
                forgotPassword.data
            }

            else -> {
                GeneralHTTP()
            }
        }
        return@withContext forgotPassword
    }

    suspend fun codeVerification(mainUser: MainUser): CustomResult<GeneralHTTP> = withContext(Dispatchers.IO) {
        val codeVerification = loginRepositoryNetwork.codeVerification(mainUser)
        when (codeVerification) {
            is CustomResult.OnSuccess -> {
                codeVerification.data
            }

            else -> {
                GeneralHTTP()
            }
        }
        return@withContext codeVerification
    }

    suspend fun changePassword(mainUser: MainUser): CustomResult<GeneralHTTP> = withContext(Dispatchers.IO) {
        val changePassword = loginRepositoryNetwork.changePassword(mainUser)
        when (changePassword) {
            is CustomResult.OnSuccess -> {
                changePassword.data
            }

            else -> {
                GeneralHTTP()
            }
        }
        return@withContext changePassword
    }
}
