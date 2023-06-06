package com.dwgu.linkive.Folder.FolderApi

import com.google.gson.annotations.SerializedName

data class ReadFoldersList(
    val folderList: List<ReadFoldersResponse>
){
    data class ReadFoldersResponse(
        @SerializedName("folder_num")
        val folderNum: Int,

        @SerializedName("users_num")
        val usersNum: Int,

        @SerializedName("name")
        val name: String,

        @SerializedName("thumbnail")
        val thumbnail: String,

        @SerializedName("isLocked")
        val isLocked: Boolean,

        @SerializedName("memoCount")
        val memoCount: Int?
    )
}
