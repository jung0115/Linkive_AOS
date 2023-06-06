package com.dwgu.linkive.LinkMemoApi.ViewLinkMemo

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

// 링크 메모 전체 조회
interface ViewLinkMemoService {
    @POST("memos")
    fun viewLinkMemo(
        @Header("Authorization") authorization: String, // 로그인으로 발급받은 AccessToken: JWT {발급받은 토큰} 형태로 입력
        @Header("refresh-token") refreshToken: String,  // 로그인으로 발급받은 RefreshToken
    ): Call<ViewLinkMemoData>
}