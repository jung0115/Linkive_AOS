package com.dwgu.linkive.LinkMemoApi.DetailLinkMemo

// 링크 세부  페이지 기본 정보
data class LinkMemoBaseInfo(
    var linkUrl: String,  // 링크 URL
    var title: String,    // 제목
    var source: String?,  // 출처 플랫폼
    var folder: String?,  // 폴더명
    var unselectPageSheet: Boolean = true, // PageSheet 선택 버튼 보여줄 것인지
)