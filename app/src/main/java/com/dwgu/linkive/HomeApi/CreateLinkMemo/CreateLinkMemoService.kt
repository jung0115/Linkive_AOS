package com.dwgu.linkive.HomeApi.CreateLinkMemo

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

// 링크 메모 생성
interface CreateLinkMemoService {
    @POST("/memo/create")
    fun addUniversityInfo(
        @Header("Authorization") Authorization: String,
        @Header("refresh-token") RefreshToken: String,
        @Body params: String
    ): Call<String>
}