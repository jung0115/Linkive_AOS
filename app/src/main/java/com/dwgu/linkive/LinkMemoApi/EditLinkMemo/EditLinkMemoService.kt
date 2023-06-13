package com.dwgu.linkive.LinkMemoApi.EditLinkMemo

import com.dwgu.linkive.LinkMemoApi.CreateLinkMemo.CreateLinkMemoData
import com.dwgu.linkive.LinkMemoApi.CreateLinkMemo.CreateLinkMemoResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/// 메모 수정 api
interface EditLinkMemoService {
    @POST("memos/edit")
    fun editLinkMemo(
        @Header("Authorization") authorization: String, // 로그인으로 발급받은 AccessToken: JWT {발급받은 토큰} 형태로 입력
        @Header("refresh-token") refreshToken: String,  // 로그인으로 발급받은 RefreshToken
        @Body params: EditLinkMemoData // 수정할 링크 메모 내용
    ): Call<String>
}