package com.dwgu.linkive.LinkMemoApi.ViewLinkMemo

import com.dwgu.linkive.LinkMemoApi.CreateLinkMemo.LinkMemoContent
import com.google.gson.annotations.SerializedName

// 링크 메모 조회 데이터
data class ViewLinkMemoData(
    @SerializedName("memoList")
    val arr: MutableList<ViewLinkMemo>,
)

data class ViewLinkMemo(
    @SerializedName("memo_num")      // 링크 메모 id
    val memo_num: Int,

    @SerializedName("owner")         // 작성자 id
    val owner: Int,

    @SerializedName("link")          // 링크 url
    val link: String,

    @SerializedName("title")         // 제목
    val title: String,

    @SerializedName("content")       // 내용
    val content: LinkMemoContent?,

    @SerializedName("date_created")  // 생성일
    val date_created: String,

    @SerializedName("folder_num")   // 폴더 번호
    val folder_num: Int?,

    @SerializedName("folder_name")  // 폴더 이름
    val folder_name: String?,
)

