package com.devpaul.estructurapublicitarias_roal.domain.usecases.login

import com.devpaul.estructurapublicitarias_roal.data.models.PrincipalUser
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.interfaces.repository.LoginRepositoryNetwork
import com.devpaul.estructurapublicitarias_roal.data.models.response.MainUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginUseCase2(private val loginRepositoryNetwork: LoginRepositoryNetwork) {

    suspend fun getMainUser(mainUser: MainUser): CustomResult<PrincipalUser> = withContext(Dispatchers.IO) {
        val login = loginRepositoryNetwork.getMainUser(mainUser)
        when (login) {
            is CustomResult.OnSuccess -> {
                login.data
            }

            else -> {
                PrincipalUser()
            }
        }
        return@withContext login
    }

    fun forgotPassword(mainUser: MainUser): CustomResult<PrincipalUser> {
        val forgotPassword = loginRepositoryNetwork.forgotPassword(mainUser)
        when (forgotPassword) {
            is CustomResult.OnSuccess -> {
                forgotPassword.data
            }

            else -> {
                PrincipalUser()
            }
        }
        return forgotPassword
    }

    fun codeVerification(mainUser: MainUser): CustomResult<PrincipalUser> {
        val codeVerification = loginRepositoryNetwork.codeVerification(mainUser)
        when (codeVerification) {
            is CustomResult.OnSuccess -> {
                codeVerification.data
            }

            else -> {
                PrincipalUser()
            }
        }
        return codeVerification
    }


}
