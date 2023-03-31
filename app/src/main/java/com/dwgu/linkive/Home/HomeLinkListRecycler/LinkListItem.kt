package com.dwgu.linkive.Home.HomeLinkListRecycler

// 링크 리스트 아이템 Data
data class LinkListItem(
    val linkTitle : String,                 // 제목
    val folderName : String?,               // 폴더명
    val thumbnailImage: String?,            // 이미지 주소 url
    val linkItemSource: String?,            // 링크 출처 플랫폼
    val linkItemForms: MutableList<String>? // 링크 아이템에 포함된 요소 표시하는 아이콘 리스트
)