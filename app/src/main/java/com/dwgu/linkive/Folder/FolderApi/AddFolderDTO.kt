package com.dwgu.linkive.Folder

import com.google.gson.annotations.SerializedName

data class AddFolderRequest(
    val name: String,
    val password: Int,
    val thumbnail: String
)

data class AddFolderResponse(
    @SerializedName("folder_num")
    val folderNum: Int
)