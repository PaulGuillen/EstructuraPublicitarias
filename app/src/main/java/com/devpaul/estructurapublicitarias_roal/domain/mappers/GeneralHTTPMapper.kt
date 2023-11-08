package com.devpaul.estructurapublicitarias_roal.domain.mappers

import com.devpaul.estructurapublicitarias_roal.data.models.entity.GeneralHTTP
import com.devpaul.estructurapublicitarias_roal.data.models.response.ResponseHttp

class GeneralHTTPMapper {

    fun map(generalHTTPResponse: ResponseHttp): GeneralHTTP {

        val mainUser = GeneralHTTP()
        mainUser.isSuccess = generalHTTPResponse.isSuccess
        mainUser.message = generalHTTPResponse.message
        mainUser.code = generalHTTPResponse.code
        mainUser.data = generalHTTPResponse.data
        mainUser.error = generalHTTPResponse.error

        return mainUser
    }


}