package hu.bme.biztonsagosgang.ciffcaff.domain.api

import hu.bme.biztonsagosgang.ciffcaff.domain.api.dto.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface APIService {

    //BULLSHIT
    @Multipart
    @POST("/caffs")
    suspend fun uploadCaff(
        @Part("name") name: RequestBody?, //requestBody
        @Part file: MultipartBody.Part?
    ): CaffDetailResponseDto

    @GET("caffs/{caffId}/download")
    suspend fun downloadCaff(@Path("caffId") caffId: Int) : Call<ResponseBody>

    ////////////////////

    @POST("/login")
    suspend fun login(@Body body: LoginRequestDto) //: LoginResponseDto todo

    @POST("/signup")
    suspend fun signup(@Body body: SignupRequestDto)

    @GET("/logout")
    suspend fun logout()

    @GET("/caffs")
    suspend fun fetchCaffList(@Query("filter") filer: String): CaffListResponseDto

    @GET("/caffs{caff_id}")
    suspend fun fetchCaffDetails(@Path("caff_id") caffId: Int): CaffDetailResponseDto

    @DELETE("/caffs/{caff_id}")
    suspend fun deleteCaff(@Path("caff_id") caffId: Int): CaffDetailResponseDto

    @POST("/caffs/{caff_id}/comment")
    suspend fun addComment(@Path("caff_id") caffId: Int, @Body body: CaffNewCommentRequestDto)

    @DELETE("/caffs/{caff_id}/comment/{comment_id}")
    suspend fun deleteComment(
        @Path("caff_id") caffId: Int,
        @Path("comment_id") commentId: Int)


}