package com.dwgu.linkive.LinkMemoApi.EditLinkMemo

import com.dwgu.linkive.LinkMemoApi.CreateLinkMemo.LinkMemoContent
import com.google.gson.annotations.SerializedName

// 링크 메모 수정 데이터
data class EditLinkMemoData(
    @SerializedName("memo_num") // 메모 번호
    val memo_num: Int,

    @SerializedName("link") // 링크 URL
    val link: String,

    @SerializedName("title") // 링크 메모 제목
    var title: String,

    @SerializedName("content")    // 링크 메모 내용
    var content: LinkMemoContent? = null,

    @SerializedName("folder_num") // 폴더
    var folder_num: Int? = null,
)
