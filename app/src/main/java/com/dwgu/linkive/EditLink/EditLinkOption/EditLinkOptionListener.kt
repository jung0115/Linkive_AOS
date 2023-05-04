package com.dwgu.linkive.EditLink.EditLinkOption

import android.net.Uri

public interface EditLinkOptionListener {

    // 링크 편집 페이지에서 아이템 삭제
    fun deleteItemListener(position: Int)

    // 링크 편집 페이지 이미지 선택
    fun selectImageListener(position: Int, imageUri: Uri)

    // 링크 편집 페이지 글, 코드, 링크, 할 일 내용 리셋
    fun resetItemListener(position: Int, itemType: String)
}