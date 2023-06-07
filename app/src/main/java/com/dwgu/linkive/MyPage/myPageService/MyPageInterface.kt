package com.dwgu.linkive.MyPage.myPageService

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST

interface MyPageInterface {

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

    //  회원정보 수정 시 ID 중복 확인
    @Headers(
        "Bearer: accessToken",
        "refresh-token: refreshToken"
    )
    @POST("users/checkDuplicatedId")
    fun postCheckDuplicatedId(
        @Body param: newId
    ): Call<result>
}