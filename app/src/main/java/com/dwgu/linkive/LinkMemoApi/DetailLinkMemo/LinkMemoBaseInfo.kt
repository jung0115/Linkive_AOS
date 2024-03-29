package com.dwgu.linkive.LinkMemoApi.DetailLinkMemo

// 링크 세부  페이지 기본 정보
data class LinkMemoBaseInfo(
    var linkUrl: String,  // 링크 URL
    var title: String,    // 제목
    var source: String?,  // 출처 플랫폼
    var folderNum: Int?,  // 폴더 번호
    var folderName: String?,  // 폴더명
    var unselectPageSheet: Boolean = true, // PageSheet 선택 버튼 보여줄 것인지
)

// 링크 편집 페이지 기본 정보
data class LinkMemoEditBaseInfo(
    var title: String,      // 제목
    var source: String?,    // 출처 플랫폼
    var pagesheetNum: Int?, // 페이지시트 번호
)