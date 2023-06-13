package com.dwgu.linkive.LinkMemoApi.CreateLinkMemo

import com.google.gson.annotations.SerializedName

// 링크 메모 생성 데이터
data class CreateLinkMemoData(
    @SerializedName("link") // 링크 URL
    val link: String,

    @SerializedName("title") // 링크 메모 제목
    val title: String,

    @SerializedName("content")    // 링크 메모 내용
    var content: LinkMemoContent? = null,

    @SerializedName("folder_num") // 폴더
    var folder_num: Int? = null,
)

data class LinkMemoContent(
    @SerializedName("pagesheet_num") // 페이지 시트
    var pagesheet_num: Int? = null,

    @SerializedName("arr") // 내용
    var arr: MutableList<String>? = null,
)

data class CreateLinkMemoResponse(
    @SerializedName("memo_num")
    val memo_num: Int,
);