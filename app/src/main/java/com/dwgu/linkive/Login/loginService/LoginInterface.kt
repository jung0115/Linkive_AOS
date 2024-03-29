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

    // 로그인
    @POST("users/login")
    fun postLogin(
        @Body param: login
    ): Call<loginTokens>

    // 회원가입
    @POST("users/signup")
    fun postSignUp(
        @Body param: signUp
    ): Call<result>

    //  회원가입 시, 이메일 인증
    @POST("users/verifyEmail/send")
    fun postSendVerifyEmail(
        @Header("email-auth-type") create: String,
        @Body param: email
    ): Call<code>

    // 아이디 찾기 시 이메일 인증
    @POST("users/verifyEmail/send")
    fun postSendVerifyEmailFindId(
        @Header("email-auth-type") findId: String,
        @Body param: email
    ): Call<code>

    // 비밀번호 찾기 시 이메일 인증
    @POST("users/verifyEmail/send")
    fun postSendVerifyEmailFindPassword(
        @Header("email-auth-type") findPw: String,
        @Body param: emailId
    ): Call<code>

    //  이메일 인증 성공 후, 이메일로 유저의 id 전송
    @POST("users/findId")
    fun postFindId(
        @Body param: email
    ): Call<result>

    //  비밀번호 찾기 시 비밀번호 변경
    @POST("users/findPassword")
    fun postFindPassword(
        @Body param: newPw
    ): Call<result>

    // 회원가입 시 ID 중복 확인
    @POST("users/checkNewId")
    fun postCheckNewId(
        @Body param: newId
    ): Call<result>

    //  이메일 중복 검사
    @POST("users/checkIsEmail")
    fun postCheckIsEmail(
        @Body param: email
    ): Call<result>

    // 아이디와 이메일이 일치하는지 검사
    @POST("users/checkIdwithEmail")
    fun postCheckIdWithEmail(
        @Body param: emailId
    ): Call<result>
}