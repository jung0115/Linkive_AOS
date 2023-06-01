package com.dwgu.linkive.LinkMemoApi

import com.dwgu.linkive.LinkMemoApi.CreateLinkMemo.CreateLinkMemoData
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface TestSignUp {
    @POST("api/users/signup")
    fun testSignup(
        @Body params: TestUserData
    ): Call<String>
}

interface TestLogin {
    @POST("api/users/login")
    fun testLogin(
        @Body params: TestLoginData
    ): Call<token>
}

data class TestUserData(
    @SerializedName("id")
    val id: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("nickname")
    val nickname: String,
)

data class TestLoginData(
    @SerializedName("id")
    val id: String,

    @SerializedName("password")
    val password: String,
)

data class token(
    @SerializedName("accessToken")
    val accessToken: String,

    @SerializedName("refreshToken")
    val refreshToken: String,
)