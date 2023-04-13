package com.dwgu.linkive.LinkView.LinkViewRecycler

// 링크 뷰 페이지에 보이는 아이템 데이터
interface LinkViewItem

// 이미지
data class LinkViewImageItem (
    val linkViewImage: String?,  // 이미지 주소 url
) : LinkViewItem

// 글
data class LinkViewTextItem (
    val linkViewText: String?,  // 글 내용
) : LinkViewItem

// 위치(주소)
data class LinkViewPlaceItem (
    val linkViewPlace1: String?,  // 도로명 주소
    val linkViewPlace2: String?,  // 지번 주소
) : LinkViewItem

// 링크
data class LinkViewLinkItem (
    val linkViewLinkTitle: String?,  // 링크 사이트 제목
    val linkViewLinkUrl: String?,    // 링크 Url
) : LinkViewItem

// 코드
data class LinkViewCodeItem (
    val linkViewCode: String?,  // 코드 내용
) : LinkViewItem

// 체크리스트(할 일)
data class LinkViewCheckboxItem (
    val linkViewCheckboxText: String?,      // 할 일 내용
    var linkViewCheckboxChecked: Boolean,  // 체크 유무
) : LinkViewItem