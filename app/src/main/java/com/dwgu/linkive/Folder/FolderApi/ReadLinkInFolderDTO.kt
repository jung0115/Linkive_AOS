package com.dwgu.linkive.Folder.FolderApi

import com.google.gson.annotations.SerializedName
import java.util.Date

data class ReadLinkInFolderRequest(
    @SerializedName("folder_num")
    val folderNum: Int,

    val password: String?
)

data class ReadLinkInFolderResponse(
    val memoList: Memo
)

data class Memo(
    @SerializedName("memo_num")
    val memoNum: Int,

    val owner: Int,

    val link: String,

    val content: Content,

    @SerializedName("date_created")
    val dateCreated: Date,

    @SerializedName("folder_num")
    val folderNum: Int?,

    @SerializedName("folder_name")
    val folderName: String?
)

data class Content(
    val arr: List<Arr>
)

data class Arr(
    val type: String,

    val value: String
)