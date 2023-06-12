package com.dwgu.linkive.LinkMemoApi.DeleteLinkMemo

import com.google.gson.annotations.SerializedName

// 링크 메모 삭제
data class DeleteLinkMemoRequest(
    @SerializedName("memo_num")      // 링크 메모 id
    val memo_num: Int,
)
