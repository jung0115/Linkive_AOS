package com.dwgu.linkive.MyPage.myPageService

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST

interface MyPageInterface {

    //  회원정보에서 현재 PW 불러오기

    @POST("users/checkCurrentPw")
    fun postCheckCurrentPw(
        @Header("Bearer")  accessToken: String,
        @Header("refresh-token") refreshToken: String,
        @Body param: currentPw
    ): Call<result>

    //  사용자 프로필 사진 불러오기
    @GET("users/profileImg")
    fun getProfileImg(
        @Header("Bearer")  accessToken: String,
        @Header("refresh-token") refreshToken: String,
    ): Call<profileImg>

    // 회원정보 불러오기
    @GET("users/userInfo")
    fun getUserInfo(
        @Header("Bearer")  accessToken: String,
        @Header("refresh-token") refreshToken: String,
    ): Call<userInfo>

    //  사용자 정보 변경 + 비밀번호 변경
    @PATCH("users/changeUserInfo")
    fun patchChangeUserInfoPw(
        @Header("Bearer")  accessToken: String,
        @Header("refresh-token") refreshToken: String,
        @Body param: changeInfoPw
    ): Call<result>

    //  사용자 정보 변경
    @PATCH("users/changeUserInfo")
    fun patchChangeUserInfo(
        @Header("Bearer")  accessToken: String,
        @Header("refresh-token") refreshToken: String,
        @Body param: changeInfo
    ): Call<result>

    //  이메일 로그인 경우, 회원 탈퇴
    @POST("users/delete")
    fun postEmailLoginDelete(
        @Header("Bearer")  accessToken: String,
        @Header("refresh-token") refreshToken: String,
        @Body param: emails
    ): Call<result>

    //  소셜 로그인 경우, 회원 탈퇴
    @POST("users/delete")
    fun postSocialLoginDelete(
        @Header("Bearer")  accessToken: String,
        @Header("refresh-token") refreshToken: String,
        @Header("social-login") soscialLogin: String,
        @Body param: email
    ): Call<result>

    //  회원정보 수정 시 ID 중복 확인
    @POST("users/checkDuplicatedId")
    fun postCheckDuplicatedId(
        @Header("Bearer")  accessToken: String,
        @Header("refresh-token") refreshToken: String,
        @Body param: newId
    ): Call<result>

    // PageSheet 생성
    @POST("pagesheets/create")
    fun postPageSheetsCreate(
        @Header("Authorization") authorization: String,
        @Header("refresh-token") refreshToken: String,
        @Body param: pageSheetCreate
    ): Call<result>

    // PageSheet 수정
    @POST("pagesheets/edit")
    fun postPageSheetsEdit(
        @Header("Authorization") authorization: String,
        @Header("refresh-token") refreshToken: String,
        @Body param: pageSheetModify
    ): Call<result>

    // PageSheet 삭제
    @POST("pagesheets/delete")
    fun postPageSheetsDelete(
        @Header("Authorization") authorization: String,
        @Header("refresh-token") refreshToken: String,
        @Body param: pageSheetNum
    ): Call<result>

    // PageSheet 전체 조회
    @POST("pagesheets")
    fun postPageSheets(
        @Header("Authorization") authorization: String,
        @Header("refresh-token") refreshToken: String
    ): Call<result>
}