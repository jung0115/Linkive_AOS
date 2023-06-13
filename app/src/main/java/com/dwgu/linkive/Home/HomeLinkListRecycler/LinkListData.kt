package com.dwgu.linkive.Home.HomeLinkListRecycler

// 링크 url로 가져온 Data
data class LinkListData(
    var linkUrl: String,            // 링크 url
    var linkTitle : String,         // 제목
    val thumbnailImage: String?,    // 이미지 주소 url
)