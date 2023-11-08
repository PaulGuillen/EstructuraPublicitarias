package com.devpaul.estructurapublicitarias_roal.domain.interfaces.repository

import com.devpaul.estructurapublicitarias_roal.data.models.entity.GeneralHTTP
import com.devpaul.estructurapublicitarias_roal.domain.custom_result.CustomResult
import com.devpaul.estructurapublicitarias_roal.data.models.response.MainUser

interface LoginRepositoryNetwork {
    fun getMainUser(mainUser: MainUser): CustomResult<GeneralHTTP>
    fun forgotPassword(mainUser: MainUser): CustomResult<GeneralHTTP>
    fun codeVerification(mainUser: MainUser): CustomResult<GeneralHTTP>
    fun changePassword(mainUser: MainUser): CustomResult<GeneralHTTP>
}