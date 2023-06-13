package com.dwgu.linkive.Folder.FolderApi

import android.provider.ContactsContract.CommonDataKinds.Nickname
import com.google.gson.annotations.SerializedName

data class AddFolderRequest(
    val name: String,
    val password: String?,
    val thumbnail: String
)

data class AddFolderResponse(
    @SerializedName("folder_num")
    val folderNum: Int
)
