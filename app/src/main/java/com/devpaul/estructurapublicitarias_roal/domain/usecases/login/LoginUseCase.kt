package com.devpaul.estructurapublicitarias_roal.domain.usecases.login

import com.devpaul.estructurapublicitarias_roal.data.models.entity.PrincipalUser
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.domain.interfaces.repository.LoginRepositoryNetwork
import com.devpaul.estructurapublicitarias_roal.data.models.response.MainUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginUseCase(private val loginRepositoryNetwork: LoginRepositoryNetwork) {

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

}
