package com.dwgu.linkive.ImageUploadApi

import com.dwgu.linkive.LinkMemoApi.CreateLinkMemo.CreateLinkMemoResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

// 이미지 업로드 api
interface ImageUploadService {
    @Multipart
    @POST("images/upload")
    fun uploadImage(
        @Header("Authorization") authorization: String, // 로그인으로 발급받은 AccessToken: JWT {발급받은 토큰} 형태로 입력
        @Header("refresh-token") refreshToken: String,  // 로그인으로 발급받은 RefreshToken
        //@Header("Content-Type") ContentType: String = "multi-part/form-data",
        @Part img : MultipartBody.Part
    ): Call<ImageUploadResponse>
}