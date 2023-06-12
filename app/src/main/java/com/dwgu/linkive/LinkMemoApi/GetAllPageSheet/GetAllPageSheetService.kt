package com.dwgu.linkive.LinkMemoApi.GetAllPageSheet

import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST

// 페이지 시트 전체 조회
interface GetAllPageSheetService {
    @POST("pagesheets")
    fun getAllPageSheet(
        @Header("Authorization") authorization: String, // 로그인으로 발급받은 AccessToken: JWT {발급받은 토큰} 형태로 입력
        @Header("refresh-token") refreshToken: String,  // 로그인으로 발급받은 RefreshToken
    ): Call<GetPageSheetListData>
}