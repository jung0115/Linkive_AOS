package com.dwgu.linkive.KakaoMapApi

import android.content.ContentValues
import android.util.Log
import com.dwgu.linkive.BuildConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

// api 통신을 위한 retrofit
private val retrofit: Retrofit = KakaoMapApiClient.getInstance()

// kakao map API Key
private const val API_KEY = BuildConfig.KAKAO_MAP_API_KEY

fun apiGetKakaoAddress(keyword: String) {
    retrofit.create(KakaoMapService::class.java)
        .getKakaoAddress(API_KEY, keyword)
        .enqueue(object : Callback<KakaoMapData> {
            override fun onResponse(call: Call<KakaoMapData>, response: Response<KakaoMapData>) {
                Log.d(ContentValues.TAG, "카카오 주소 조회 결과 -------------------------------------------")
                Log.d(ContentValues.TAG, "onResponse: ${response.body().toString()}")
            }

            override fun onFailure(call: Call<KakaoMapData>, t: Throwable) {
                Log.d(ContentValues.TAG, "카카오 주소 조회 결과 fail -------------------------------------------")
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
}