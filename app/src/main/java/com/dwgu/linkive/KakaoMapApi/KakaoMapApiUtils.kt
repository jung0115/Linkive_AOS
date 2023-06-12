package com.dwgu.linkive.KakaoMapApi

import android.content.ContentValues
import android.util.Log
import com.dwgu.linkive.BuildConfig
import com.dwgu.linkive.EditLink.EditPlaceRecyclerview.SelectPlaceItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

// api 통신을 위한 retrofit
private val retrofit: Retrofit = KakaoMapApiClient.getInstance()

// kakao map API Key
private const val API_KEY = BuildConfig.KAKAO_MAP_API_KEY

fun apiGetKakaoAddress(
    keyword: String,
    addPlaceList: (item: SelectPlaceItem) -> Unit ) {
    retrofit.create(KakaoMapService::class.java)
        .getKakaoAddress(API_KEY, keyword)
        .enqueue(object : Callback<KakaoMapData> {
            override fun onResponse(call: Call<KakaoMapData>, response: Response<KakaoMapData>) {
                Log.d(ContentValues.TAG, "카카오 주소 조회 결과 -------------------------------------------")
                Log.d(ContentValues.TAG, "onResponse: ${response.body().toString()}")

                val body: KakaoMapData = response.body()!!
                val placeList = body.documents
                for(i in 0 until placeList.size) {
                    var selected = false
                    // 첫 번째 주소 선택
                    if(i == 0) selected = true

                    // 주소 선택지 추가
                    addPlaceList(
                        SelectPlaceItem(
                            placeList[i].place_name,
                            placeList[i].road_address_name,
                            placeList[i].address_name,
                            selected
                        )
                    )
                }
            }

            override fun onFailure(call: Call<KakaoMapData>, t: Throwable) {
                Log.d(ContentValues.TAG, "카카오 주소 조회 결과 fail -------------------------------------------")
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
}