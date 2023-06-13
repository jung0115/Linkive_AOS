package com.dwgu.linkive.KakaoMapApi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

// 카카오맵 api Service
interface KakaoMapService {
    @GET("v2/local/search/keyword.json")
    fun getKakaoAddress(
        @Header("Authorization") apiKey: String,
        @Query("query") keyword: String, // 검색 키워드
        @Query("size") size: Int = 3     // 검색 결과 갯수
    ): Call<KakaoMapData>
}