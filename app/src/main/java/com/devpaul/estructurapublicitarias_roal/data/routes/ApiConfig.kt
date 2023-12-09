package com.devpaul.estructurapublicitarias_roal.data.routes

import com.devpaul.estructurapublicitarias_roal.data.models.response.GetWorkerResponse
import com.devpaul.estructurapublicitarias_roal.data.models.request.ValidateImageByPhotoRequest
import com.devpaul.estructurapublicitarias_roal.data.models.request.ValidationEPPRequest
import com.devpaul.estructurapublicitarias_roal.data.models.request.WorkerRequest
import com.devpaul.estructurapublicitarias_roal.data.models.response.MainUser
import com.devpaul.estructurapublicitarias_roal.data.models.response.OptionsResponse
import com.devpaul.estructurapublicitarias_roal.data.models.response.PrincipalListWorkerResponse
import com.devpaul.estructurapublicitarias_roal.data.models.response.ResponseHttp
import com.devpaul.estructurapublicitarias_roal.data.models.response.ValidateImageByPhotoResponse
import com.devpaul.estructurapublicitarias_roal.data.models.response.ValidationEPPResponse
import com.devpaul.estructurapublicitarias_roal.data.models.response.WorkerReportByUserResponse
import com.devpaul.estructurapublicitarias_roal.data.models.response.WorkersResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiConfig {

    @POST("roal_app_login/login")
    fun mainUserEndPoints(@Body mainUser: MainUser): Call<ResponseHttp>

    @GET("getOptions")
    fun getOptions(
    ): Call<List<OptionsResponse>>

    @GET("get_worker/{dni}")
    fun getWorkers(
        @Path("dni") dni: String
    ): Call<GetWorkerResponse>

    @POST("register_worker")
    fun creatingWorkers(
        @Body workerUser: WorkersResponse
    ): Call<ResponseHttp>

    @POST("register_worker")
    fun createWorker(
        @Body workerUser: WorkerRequest
    ): Call<ResponseHttp>

    @DELETE("delete_worker/{dni}")
    fun deleteWorker(
        @Path("dni") dni: String
    ): Call<ResponseHttp>

    @POST("get_data_from_photo")
    fun validateImageByPhoto(
        @Body validateImageByPhotoRequest: ValidateImageByPhotoRequest
    ): Call<ValidateImageByPhotoResponse>

    @POST("https://run.mocky.io/v3/d52e5f8e-e09c-45c6-85ad-32749b9dd68b")
    // @POST("https://run.mocky.io/v3/98ccd3e7-0dfe-4dca-90e0-2be122cea9f5")
    fun validationEPP(
        @Body validationRequest: ValidationEPPRequest
    ): Call<ValidationEPPResponse>

    //@GET("allWorkers")
    @GET("https://run.mocky.io/v3/15ea1e83-b4aa-46fd-9a76-67e8ae4c6c80")
    fun allWorkers(
    ): Call<List<PrincipalListWorkerResponse>>

    @GET("https://run.mocky.io/v3/93a59216-3461-4c8d-bae1-d3fcd7f3e9b5")
    fun reportWorker(
        @Query("document") document: String? = null
    ): Call<WorkerReportByUserResponse>

    @GET("https://run.mocky.io/v3/71020a84-b380-4e91-8b16-bf3c4addec78")
    fun listWorkers(
    ): Call<WorkerReportByUserResponse>
}