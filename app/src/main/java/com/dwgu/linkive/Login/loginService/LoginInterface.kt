package com.dwgu.linkive.Login.loginService

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST


interface LoginInterface {

    // Login
    @POST("users/login")
    fun postLogin(
        @Body param: login
    ): Call<loginTokens>

    // Signup
    @POST("users/signup")
    fun postSignUp(
        @Body param: signUp
    ): Call<result>

    // Delete
    @POST("users/delete")
    fun postUserDelete(
        @Body param: emails
    ): Call<result>

    // EmailLogin
    @Headers(
        "Bearer: accessToken",
        "refresh-token: refreshToken"
    )
    @POST("users/delete")
    fun postEmailLogin(
        @Body param: emails
    ): Call<result>

    // SocialLogin
    @Headers(
        "Bearer: accessToken",
        "refresh-token: refreshToken",
        "social-login: socialLogin"
    )
    @POST("users/delete")
    fun postSocialLogin(
        @Body param: email
    ): Call<result>

    // SendVerifyEmail - signup
    @Headers("email-auth-type: create")
    @POST("users/verifyEmail/send")
    fun postSendVerifyEmail(
        @Body param: email
    ): Call<code>

    // SendVerifyEmail - find id
    @Headers("email-auth-type: findId")
    @POST("users/verifyEmail/send")
    fun postSendEmailFindId(
        @Body param: email
    ): Call<code>

    // SendVerifyEmail - find pw
    @Headers("email-auth-type: findPw")
    @POST("users/verifyEmail/send")
    fun postSendEmailFindPassword(
        @Body param: findPw
    ): Call<code>

    // FindId
    @POST("users/findId")
    fun postFindId(
        @Body param: email
    ): Call<result>

    // FindPassword
    @POST("users/changePassword")
    fun postFindPassword(
        @Body param: login
    ): Call<result>

    // ChangeUserInfo - changePw
    @PATCH("users/changeUserInfo")
    @Headers(
        "Bearer: accessToken",
        "refresh-token: refreshToken"
    )
    fun patchChangeUserInfoPw(
        @Body param: changeInfoPw
    ): Call<result>

    // ChangeUserInfo
    @PATCH("users/changeUserInfo")
    @Headers(
        "Bearer: accessToken",
        "refresh-token: refreshToken"
    )
    fun patchChangeUserInfo(
        @Body param: changeInfo
    ): Call<result>

    // CheckDuplicatedId
    @Headers(
        "Bearer: accessToken",
        "refresh-token: refreshToken"
    )
    @POST("users/checkDuplicatedId")
    fun postCheckDuplicatedId(
        @Body param: newId
    ): Call<result>

    // CheckIsEmail
    @POST("users/checkIdwithEmail")
    fun postCheckIdEmail(
        @Body param: email
    ): Call<result>

    // CheckCurrentPw
    @POST("users/checkCurrentPw")
    fun postCheckCurrentPw(
        @Body param: currentPw
    ): Call<result>

    // GetProfileImg
    @Headers(
        "Bearer: accessToken",
        "refresh-token: refreshToken"
    )
    @GET("users/profileImg")
    fun getProfileImg(): Call<profileImg>

    // UserInfo
    @Headers(
        "Bearer: accessToken",
        "refresh-token: refreshToken"
    )
    @GET("users/userInfo")
    fun getUserInfo(): Call<userInfo>
}