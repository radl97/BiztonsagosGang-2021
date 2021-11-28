package hu.bme.biztonsagosgang.ciffcaff.domain.api

import okhttp3.MultipartBody
import okhttp3.RequestBody

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface APIService {
    @Multipart
    @POST("upload")
    fun uploadCaff(
        @Part("name") name: RequestBody?, //requestBody
        @Part file: MultipartBody.Part?
    )

    @GET("caffs/{caffId}/download")
    fun downloadCaff(@Path("caffId") caffId: Int) : Call<ResponseBody>
}