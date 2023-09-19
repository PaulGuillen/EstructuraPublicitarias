package com.devpaul.estructurapublicitarias_roal.domain.interfaces.repository

import com.devpaul.estructurapublicitarias_roal.data.models.PrincipalUser
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.data.models.response.MainUser

interface LoginRepositoryNetwork {
    fun getMainUser(mainUser: MainUser): CustomResult<PrincipalUser>
    fun forgotPassword(mainUser: MainUser): CustomResult<PrincipalUser>
    fun codeVerification(mainUser: MainUser): CustomResult<PrincipalUser>
    fun changePassword(mainUser: MainUser): CustomResult<PrincipalUser>
}