package com.dwgu.linkive.LinkMemoApi.DetailLinkMemo

import com.dwgu.linkive.LinkMemoApi.ViewLinkMemo.ViewLinkMemo
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

// 메모 번호로 메모 내용 조회
interface DetailLinkMemoService {
    @POST("memos/{memos_num}")
    fun detailLinkMemo(
        @Header("Authorization") authorization: String, // 로그인으로 발급받은 AccessToken: JWT {발급받은 토큰} 형태로 입력
        @Header("refresh-token") refreshToken: String,  // 로그인으로 발급받은 RefreshToken
        @Path("memos_num") memosNum: Int // 조회할 메모 번호
    ): Call<ViewLinkMemo>
}