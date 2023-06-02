package com.dwgu.linkive.Folder.FolderApi

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface FolderInterface {
    @POST("folders/create")
    fun createFolder(
        @Header("Authorization") accessToken: String?,
        @Header("refresh-token") refreshToken: String?,
        @Body addFolderRequest: AddFolderRequest
    ) : Call<AddFolderResponse>

    @POST("folders")
    fun readFolder(
        @Header("Authorization") accessToken: String?,
        @Header("refresh-token") refreshToken: String?
    )
    : Call<ReadFoldersList>

    // 지우기
    @POST("users/signup")
    fun signUp(
        @Body signUpRequest: SignUpRequest
    ) : Call<String>

    @POST("users/login")
    fun login(
        @Body loginRequest: LoginRequest
    ) : Call<LoginRequest>
}