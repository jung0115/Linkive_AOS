package com.dwgu.linkive.HomeApi.CreateLinkMemo

import com.google.gson.annotations.SerializedName

// 링크 메모 생성 데이터
data class CreateLinkMemoData(
    @SerializedName("link") // 링크 URL
    val link: String,

    @SerializedName("title") // 링크 메모 제목
    val title: String,

    @SerializedName("content")    // 링크 메모 내용
    val content: String,

    @SerializedName("folder_num") // 폴더
    val folder_num: Int,
)
