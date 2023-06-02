package com.dwgu.linkive.Folder.FolderApi

import android.provider.ContactsContract.CommonDataKinds.Nickname
import com.google.gson.annotations.SerializedName

data class AddFolderRequest(
    val name: String,
    val password: Int?,
    val thumbnail: String
)

data class AddFolderResponse(
    @SerializedName("folder_num")
    val folderNum: Int
)


//지우기
data class SignUpRequest(
    val id: String,

    val password: String,

    val email: String,

    val nickname: String
)
data class LoginRequest(
    val id: String,

    val password: String
)
data class LoginResponse(
    val accessToken: String,

    val refreshToken: String
)
