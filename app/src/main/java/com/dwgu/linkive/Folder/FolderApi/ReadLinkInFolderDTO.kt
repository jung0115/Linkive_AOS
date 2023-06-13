package com.dwgu.linkive.Folder.FolderApi

import com.google.gson.annotations.SerializedName
import java.util.Date

data class ReadLinkInFolderRequest(
//    @SerializedName("folder_num")
//    val folderNum: Int,
    val password: String?
)

data class ReadLinkInFolderResponse(
    @SerializedName("folder_num")
    val folderNum: Int,
    val memoList: List<Memo>
)

data class Memo(
    @SerializedName("memo_num")
    val memoNum: Int,

    val owner: Int,

    val link: String,

    val title: String,

    val content: Content,

    @SerializedName("date_created")
    val dateCreated: String,

    @SerializedName("folder_num")
    val folderNum: Int?,

)

data class Content(
    @SerializedName("pagesheet_num")
    val pagesheetNum: Int,
    val arr: List<Item>
)

data class Item(
    val type: String,
    val value: String,
    @SerializedName("is_checked")
    val isChecked: String? = null,
    @SerializedName("road_address")
    val roadAddress: String? = null,
    @SerializedName("land_address")
    val landAddress: String? = null,
    val url: String? = null
)