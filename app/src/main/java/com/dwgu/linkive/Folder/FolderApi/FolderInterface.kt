package com.dwgu.linkive.Folder.FolderApi

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

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

    @POST("folders/delete")
    fun removeFolder(
        @Header("Authorization") accessToken: String?,
        @Header("refresh-token") refreshToken: String?,
        @Body folderRequest: RemoveFolderRequest
    )
    : Call<String>

    @POST("memos/folders/{folder_num}")
    fun readLinkInFolder(
        @Path("folder_num") folderNum: Int,
        @Header("Authorization") accessToken: String?,
        @Header("refresh-token") refreshToken: String?,
        @Body readLinkInFolderRequest: ReadLinkInFolderRequest
//        @Body password: String?
    )
    : Call<ReadLinkInFolderResponse>

    @POST("folders/edit")
    fun editFolder(
        @Header("Authorization") accessToken: String?,
        @Header("refresh-token") refreshToken: String?,
        @Body editFolderRequest: EditFolderRequest
    )
    : Call<String>
}