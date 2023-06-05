package com.dwgu.linkive.LinkMemoApi.CreateLinkMemo

import android.content.ContentValues.TAG
import android.util.Log
import com.dwgu.linkive.Api.ApiClient
import com.dwgu.linkive.LinkMemoApi.TestClient
import com.dwgu.linkive.LinkMemoApi.TestLogin
import com.dwgu.linkive.LinkMemoApi.TestLoginData
import com.dwgu.linkive.LinkMemoApi.TestSignUp
import com.dwgu.linkive.LinkMemoApi.TestUserData
import com.dwgu.linkive.LinkMemoApi.ViewLinkMemo.ViewLinkMemoService
import com.dwgu.linkive.LinkMemoApi.token
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

// api 통신을 위한 retrofit
private val retrofit: Retrofit = ApiClient.getInstance()

// 현재 로그인 된 사용자 jwt
//private var authorization: String? = null
//private var refreshToken: String? = null

private var authorization: String? = null
private var refreshToken: String? = null

// 링크 url 내용 조회한 걸로 링크 메모 생성
fun apiCreateLinkMemo(linkMemo: CreateLinkMemoData) {
    retrofit.create(CreateLinkMemoService::class.java)
        .addLinkMemo(authorization = authorization!!, refreshToken = refreshToken!!, linkMemo)
        .enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.d(TAG, "링크 메모 추가 결과 -------------------------------------------")
                Log.d(TAG, "onResponse: ${response.body().toString()}")
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d(TAG, "링크 메모 추가 결과 fail -------------------------------------------")
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
}

// 링크 메모 전체 조회
fun viewCreateLinkMemo() {
    retrofit.create(ViewLinkMemoService::class.java)
        .viewLinkMemo(authorization = authorization!!, refreshToken = refreshToken!!)
        .enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.d(TAG, "링크 메모 조회 결과 -------------------------------------------")
                Log.d(TAG, "onResponse: ${response.body().toString()}")
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d(TAG, "링크 메모 조회 결과 fail -------------------------------------------")
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
}

fun testSignUp() {
    val user = TestUserData("jung0987", "test1234", "test1234@naver.com", "testjm")
    retrofit.create(TestSignUp::class.java)
        .testSignup(user)
        .enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.d(TAG, "테스트용 계정 가입 -------------------------------------------")
                Log.d(TAG, "onResponse: ${response.body().toString()}")
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d(TAG, "테스트용 계정 가입 fail -------------------------------------------")
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
}

fun testLogin() {
    val user = TestLoginData("jung0987", "test1234")
    retrofit.create(TestLogin::class.java)
        .testLogin(user)
        .enqueue(object : Callback<token> {
            override fun onResponse(call: Call<token>, response: Response<token>) {
                Log.d(TAG, "테스트용 계정 로그인 -------------------------------------------")
                Log.d(TAG, "onResponse: ${response.body().toString()}")

                authorization = response.body()!!.accessToken
                refreshToken = response.body()!!.refreshToken

                viewCreateLinkMemo()
            }

            override fun onFailure(call: Call<token>, t: Throwable) {
                Log.d(TAG, "테스트용 계정 로그인 fail -------------------------------------------")
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
}