package com.dwgu.linkive.SearchApi

import android.content.ContentValues.TAG
import android.util.Log
import com.dwgu.linkive.Api.ApiClient
import com.dwgu.linkive.LinkMemoApi.ViewLinkMemo.ViewLinkMemo
import com.dwgu.linkive.Login.GloabalApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

// api 통신을 위한 retrofit
private val retrofit: Retrofit = ApiClient.getInstance()

// 현재 로그인 된 사용자 token
private var authorization: String? = null
private var refreshToken: String? = null

// token 정보 세팅
fun setTokenForSearch() {
    authorization = "JWT " + GloabalApplication.prefs.getString("accessToken", "")
    refreshToken = GloabalApplication.prefs.getString("refreshToken", "")
}

// 검색
fun apiSearch(
    searchData: SearchRequest,
    setSearchResult: (searchResult: MutableList<ViewLinkMemo>?) -> Unit
    ) {
    retrofit.create(GetSearchService::class.java)
        .getSearch(authorization = authorization!!, refreshToken = refreshToken!!, searchData)
        .enqueue(object : Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                Log.d(TAG, "검색 결과 -------------------------------------------")
                Log.d(TAG, "onResponse: ${response.body().toString()}")

                var body: SearchResponse = response.body()!!
                // 검색 결과 전달
                setSearchResult(body.searchResult)
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                Log.d(TAG, "검색 결과 fail -------------------------------------------")
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
}

// 전체 검색
fun apiSearchAll(
    searchData: SearchRequest,
    setSearchResult: (searchResult: SearchAllData?) -> Unit
) {
    retrofit.create(GetSearchAllService::class.java)
        .getSearchAll(authorization = authorization!!, refreshToken = refreshToken!!, searchData)
        .enqueue(object : Callback<SearchResponseAll> {
            override fun onResponse(call: Call<SearchResponseAll>, response: Response<SearchResponseAll>) {
                Log.d(TAG, "검색 결과 전체 -------------------------------------------")
                Log.d(TAG, "onResponse: ${response.body().toString()}")

                var body: SearchResponseAll = response.body()!!
                // 검색 결과 전달
                setSearchResult(body.searchResult)
            }

            override fun onFailure(call: Call<SearchResponseAll>, t: Throwable) {
                Log.d(TAG, "검색 결과 전체 fail -------------------------------------------")
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
}