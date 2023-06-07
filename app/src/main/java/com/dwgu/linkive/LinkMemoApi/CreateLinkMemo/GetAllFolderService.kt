package com.dwgu.linkive.LinkMemoApi.CreateLinkMemo

import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST

// 폴더 전체 가져오기
interface GetAllFolderService {
    @POST("folders")
    fun getAllFolders(
        @Header("Authorization") authorization: String, // 로그인으로 발급받은 AccessToken: JWT {발급받은 토큰} 형태로 입력
        @Header("refresh-token") refreshToken: String,  // 로그인으로 발급받은 RefreshToken
    ): Call<GetAllFolderData>
}