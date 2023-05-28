package com.dwgu.linkive.Folder.FolderApi

import com.dwgu.linkive.Folder.AddFolderRequest
import com.dwgu.linkive.Folder.AddFolderResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface FolderInterface {
    @POST("folder/create")
    fun createFolder(
        @Body addFolderRequest: AddFolderRequest
    ) : Call<AddFolderResponse>
}