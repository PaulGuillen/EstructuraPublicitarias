package com.devpaul.estructurapublicitarias_roal.data.routes

import com.devpaul.estructurapublicitarias_roal.data.models.request.ValidationEPPRequest
import com.devpaul.estructurapublicitarias_roal.data.models.response.OptionsResponse
import com.devpaul.estructurapublicitarias_roal.data.models.response.ResponseHttp
import com.devpaul.estructurapublicitarias_roal.data.models.response.ValidationEPPResponse
import com.devpaul.estructurapublicitarias_roal.data.models.response.WorkersResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiConfig {

    @GET("https://run.mocky.io/v3/0645b54d-ccbe-4e49-a61a-7eb3f702982f")
    fun getOptions(
    ): Call<List<OptionsResponse>>

    @GET("get_worker/{dni}")
    fun getWorkers(
        @Query("dni") dni: String
    ): Call<WorkersResponse>

    @POST("register_worker")
    fun creatingWorkers(
        @Body workerUser: WorkersResponse
    ): Call<ResponseHttp>

    @DELETE("delete_worker/{dni}")
    fun deleteWorker(
        @Path("dni") dni: String
    ): Call<ResponseHttp>

    @POST("getDataFromPhoto")
    fun consultByPhoto(
        @Body workerUser: WorkersResponse
    ): Call<WorkersResponse>

    @POST("https://run.mocky.io/v3/b3454421-a4a4-48bc-b1fb-4e3fee2dadd4")
    fun validationEPP(
        @Body validationRequest: ValidationEPPRequest
    ): Call<ValidationEPPResponse>
}