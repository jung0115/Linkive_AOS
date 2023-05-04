package com.dwgu.linkive.Home.HomeLinkListRecycler

// 링크 아이템 Data
data class LinkListData(
    val linkUrl: String,            // 링크 url
    val linkTitle : String,         // 제목
    val folderName : String?,       // 폴더명
    val thumbnailImage: String?,    // 이미지 주소 url
    val linkItemSource: String?,    // 링크 출처 플랫폼
)