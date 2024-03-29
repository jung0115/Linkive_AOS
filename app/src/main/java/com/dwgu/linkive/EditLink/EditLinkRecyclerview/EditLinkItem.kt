package com.dwgu.linkive.EditLink.EditLinkRecyclerview

import android.net.Uri

// 링크 편집 페이지에 보이는 아이템 데이터
interface EditLinkItem {
    var position: Int
}

// 이미지
data class EditLinkImageItem (
    var editLinkImage: String?,      // 이미지 주소 url
    var editLinkImageUri: Uri?,      // 이미지 주소 Bitmap -> 이미지 편집 중에만 사용.
    override var position: Int,

) : EditLinkItem

// 글
data class EditLinkTextItem (
    var editLinkText: String?,  // 글 내용
    override var position: Int,
) : EditLinkItem

// 위치(주소)
data class EditLinkPlaceItem (
    var editLinkPlace1: String?,  // 도로명 주소
    var editLinkPlace2: String?,  // 지번 주소
    override var position: Int,
) : EditLinkItem

// 링크
data class EditLinkLinkItem (
    var editLinkLinkTitle: String?,  // 링크 사이트 제목
    var editLinkLinkUrl: String?,    // 링크 Url
    override var position: Int,
) : EditLinkItem

// 코드
data class EditLinkCodeItem (
    var editLinkCode: String?,  // 코드 내용
    override var position: Int,
) : EditLinkItem

// 체크리스트(할 일)
data class EditLinkCheckboxItem (
    var editLinkCheckboxText: String?,     // 할 일 내용
    var editLinkCheckboxChecked: Boolean,  // 체크 유무
    override var position: Int,
) : EditLinkItem