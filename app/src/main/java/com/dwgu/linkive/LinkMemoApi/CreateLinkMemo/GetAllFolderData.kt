package com.dwgu.linkive.LinkMemoApi.CreateLinkMemo

import com.google.gson.annotations.SerializedName

data class GetAllFolderData(
    @SerializedName("folderList") // 폴더 리스트
    val folderList: MutableList<FolderList>? = null,
)

data class FolderList(
    @SerializedName("folder_num") // 폴더 번호
    val folder_num: Int,

    @SerializedName("users_num") // 유저 번호
    val users_num: Int,

    @SerializedName("name")      // 폴더명
    val name: String,

    @SerializedName("thumbnail") // 폴더 썸네일
    var thumbnail: String? = null,

    @SerializedName("isLocked")  // 폴더 잠금 유무
    val isLocked: Boolean,

    @SerializedName("memoCount") // 메모 갯수
    val memoCount: Int,
)