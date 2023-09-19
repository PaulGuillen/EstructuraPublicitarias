package com.devpaul.estructurapublicitarias_roal.domain.usecases

import android.content.Context
import com.devpaul.estructurapublicitarias_roal.data.models.PrincipalUser
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.interfaces.repository.LoginRepositoryNetwork
import com.devpaul.estructurapublicitarias_roal.data.models.response.MainUser
import com.devpaul.estructurapublicitarias_roal.domain.utils.SharedPref

class LoginUseCase(
    var context: Context,
    private val loginRepositoryNetwork: LoginRepositoryNetwork
) {
    private val prefs = SharedPref(context)

    fun getMainUser(mainUser: MainUser): CustomResult<PrincipalUser> {
        val login = loginRepositoryNetwork.getMainUser(mainUser)
        when (login) {
            is CustomResult.OnSuccess -> {
                saveLogin(login.data)
            }

            else -> {
                saveLogin(PrincipalUser())
            }
        }
        return login
    }

    fun forgotPassword(mainUser: MainUser): CustomResult<PrincipalUser> {
        val forgotPassword = loginRepositoryNetwork.forgotPassword(mainUser)
        when (forgotPassword) {
            is CustomResult.OnSuccess -> {
                saveForgotPassword(forgotPassword.data)
            }

            else -> {
                saveForgotPassword(PrincipalUser())
            }
        }
        return forgotPassword
    }

    fun codeVerification(mainUser: MainUser): CustomResult<PrincipalUser> {
        val codeVerification = loginRepositoryNetwork.codeVerification(mainUser)
        when (codeVerification) {
            is CustomResult.OnSuccess -> {
                saveCodeVerification(codeVerification.data)
            }

            else -> {
                saveCodeVerification(PrincipalUser())
            }
        }
        return codeVerification
    }

    fun changePassword(mainUser: MainUser): CustomResult<PrincipalUser> {
        val changePassword = loginRepositoryNetwork.changePassword(mainUser)
        when (changePassword) {
            is CustomResult.OnSuccess -> {
                saveChangePassword(changePassword.data)
            }

            else -> {
                saveChangePassword(PrincipalUser())
            }
        }
        return changePassword
    }

    private fun saveLogin(principalUser: PrincipalUser) {
        if (principalUser.code == 200) {
            prefs.saveJsonObject("PrincipalUser", principalUser)
        }
    }

    private fun saveForgotPassword(principalUser: PrincipalUser) {
        if (principalUser.code == 200) {
            prefs.saveJsonObject("SaveForgotPassword", principalUser)
        }
    }

    private fun saveCodeVerification(principalUser: PrincipalUser) {
        if (principalUser.code == 200) {
            prefs.saveJsonObject("CodeVerification", principalUser)
        }
    }

    private fun saveChangePassword(principalUser: PrincipalUser) {
        if (principalUser.code == 200) {
            prefs.saveJsonObject("ChangePassword", principalUser)
        }
    }
}
