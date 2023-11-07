package com.devpaul.estructurapublicitarias_roal.domain.mappers

import com.devpaul.estructurapublicitarias_roal.data.models.entity.PrincipalUser
import com.devpaul.estructurapublicitarias_roal.data.models.response.ResponseHttp

class MainUserMapper {

    fun map(loginResponse: ResponseHttp): PrincipalUser {

        val mainUser = PrincipalUser()
        mainUser.isSuccess = loginResponse.isSuccess
        mainUser.message = loginResponse.message
        mainUser.code = loginResponse.code
        mainUser.data = loginResponse.data
        mainUser.error = loginResponse.error

        return mainUser
    }

}