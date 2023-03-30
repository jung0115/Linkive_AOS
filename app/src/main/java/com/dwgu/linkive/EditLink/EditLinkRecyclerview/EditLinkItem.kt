package com.dwgu.linkive.EditLink.EditLinkRecyclerview

// 링크 편집 페이지에 보이는 아이템 데이터
interface EditLinkItem

// 이미지
data class EditLinkImageItem (
    var editLinkImage: String?,  // 이미지 주소 url
) : EditLinkItem

// 글
data class EditLinkTextItem (
    var editLinkText: String?,  // 글 내용
) : EditLinkItem

// 위치(주소)
data class EditLinkPlaceItem (
    var editLinkPlace1: String?,  // 도로명 주소
    var editLinkPlace2: String?,  // 지번 주소
) : EditLinkItem

// 링크
data class EditLinkLinkItem (
    var editLinkLinkTitle: String?,  // 링크 사이트 제목
    var editLinkLinkUrl: String?,    // 링크 Url
) : EditLinkItem

// 코드
data class EditLinkCodeItem (
    var editLinkCode: String?,  // 코드 내용
) : EditLinkItem

// 체크리스트(할 일)
data class EditLinkCheckboxItem (
    var editLinkCheckboxText: String?,     // 할 일 내용
    var editLinkCheckboxChecked: Boolean,  // 체크 유무
) : EditLinkItem