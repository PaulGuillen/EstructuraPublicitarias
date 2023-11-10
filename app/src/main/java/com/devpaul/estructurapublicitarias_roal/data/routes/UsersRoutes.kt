package com.devpaul.estructurapublicitarias_roal.data.routes

import com.devpaul.estructurapublicitarias_roal.data.models.response.MainUser
import com.devpaul.estructurapublicitarias_roal.data.models.response.ResponseHttp
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UsersRoutes {

    @POST("/roal_app_login/login")
    fun mainUserEndPoints(@Body mainUser: MainUser): Call<ResponseHttp>

}