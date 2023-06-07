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

    //  이메일 로그인 경우, 회원 탈퇴
    @Headers(
        "Bearer: accessToken",
        "refresh-token: refreshToken"
    )
    @POST("users/delete")
    fun postEmailLoginDelete(
        @Body param: emails
    ): Call<result>

    //  소셜 로그인 경우, 회원 탈퇴
    @Headers(
        "Bearer: accessToken",
        "refresh-token: refreshToken",
        "social-login: socialLogin"
    )
    @POST("users/delete")
    fun postSocialLoginDelete(
        @Body param: email
    ): Call<result>

    //  회원가입 시, 이메일 인증
    @Headers("email-auth-type: create")
    @POST("users/verifyEmail/send")
    fun postSendVerifyEmail(
        @Body param: email
    ): Call<code>

    // 아이디 찾기 시 이메일 인증
    @Headers("email-auth-type: find")
    @POST("users/verifyEmail/send")
    fun postSendVerifyEmailFindId(
        @Body param: email
    ): Call<code>

    // 비밀번호 찾기 시 이메일 인증
    @Headers("email-auth-type: findPw")
    @POST("users/verifyEmail/send")
    fun postSendVerifyEmailFindPassword(
        @Body param: findPw
    ): Call<code>

    //  이메일 인증 성공 후, 이메일로 유저의 id 전송
    @POST("users/findId")
    fun postFindId(
        @Body param: email
    ): Call<result>

    //  비밀번호 찾기 시 비밀번호 변경
    @POST("users/changePassword")
    fun postFindPassword(
        @Body param: newPw
    ): Call<result>

    //  사용자 정보 변경 + 비밀번호 변경
    @PATCH("users/changeUserInfo")
    @Headers(
        "Bearer: accessToken",
        "refresh-token: refreshToken"
    )
    fun patchChangeUserInfoPw(
        @Body param: changeInfoPw
    ): Call<result>

    //  사용자 정보 변경
    @PATCH("users/changeUserInfo")
    @Headers(
        "Bearer: accessToken",
        "refresh-token: refreshToken"
    )
    fun patchChangeUserInfo(
        @Body param: changeInfo
    ): Call<result>

    // 회원가입 시 ID 중복 확인
    @POST("users/checkNewId")
    fun postCheckNewId(
        @Body param: newId
    ): Call<result>

    //  회원정보 수정 시 ID 중복 확인
    @Headers(
        "Bearer: accessToken",
        "refresh-token: refreshToken"
    )
    @POST("users/checkDuplicatedId")
    fun postCheckDuplicatedId(
        @Body param: newId
    ): Call<result>

    //  이메일 중복 검사
    @POST("users/checkIsEmail")
    fun postCheckIsEmail(
        @Body param: email
    ): Call<result>

    //  회원정보에서 현재 PW 불러오기
    @Headers(
        "Bearer: accessToken",
        "refresh-token: refreshToken"
    )
    @POST("users/checkCurrentPw")
    fun postCheckCurrentPw(
        @Body param: currentPw
    ): Call<result>

    //  사용자 프로필 사진 불러오기
    @Headers(
        "Bearer: accessToken",
        "refresh-token: refreshToken"
    )
    @GET("users/profileImg")
    fun getProfileImg(): Call<profileImg>

    // 회원정보 불러오기
    @Headers(
        "Bearer: accessToken",
        "refresh-token: refreshToken"
    )
    @GET("users/userInfo")
    fun getUserInfo(): Call<userInfo>
}