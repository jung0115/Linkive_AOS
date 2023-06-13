package com.dwgu.linkive.Folder.FolderApi

import com.google.gson.annotations.SerializedName

data class RemoveFolderRequest(
    @SerializedName("folder_num")
    val folderNum: Int
)

//data class RemoveFolderResponse(
//    @SerializedName("folder_num")
//    val folderNum: Int
//)
