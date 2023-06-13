package com.dwgu.linkive.Folder.FolderApi

import com.google.gson.annotations.SerializedName

data class EditFolderRequest (
    @SerializedName("folder_num")
    val folderNum: Int,

    val name: String,

    val password: String?,

    val thumbnail: String
)