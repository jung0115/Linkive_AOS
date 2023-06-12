package com.dwgu.linkive.SearchApi

import com.dwgu.linkive.LinkMemoApi.ViewLinkMemo.ViewLinkMemo
import com.google.gson.annotations.SerializedName

data class SearchRequest(
    @SerializedName("keyword") // 키워드
    val keyword: String,

    @SerializedName("method")  // 검색 탭
    val method: String?,
)

data class SearchResponse(
    @SerializedName("searchResult")
    val searchResult: MutableList<ViewLinkMemo>?,
)

data class SearchResponseAll(
    @SerializedName("searchResult")
    val searchResult: SearchAllData,
)
data class SearchAllData(
    @SerializedName("title")
    val title:MutableList<ViewLinkMemo>?,

    @SerializedName("content")
    val content:MutableList<ViewLinkMemo>?,

    @SerializedName("folder")
    val folder:MutableList<ViewLinkMemo>?,
)