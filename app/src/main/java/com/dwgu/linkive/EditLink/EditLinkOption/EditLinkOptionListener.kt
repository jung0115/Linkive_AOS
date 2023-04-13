package com.dwgu.linkive.EditLink.EditLinkOption

import android.net.Uri

public interface EditLinkOptionListener {

    // 링크 편집 페이지에서 아이템 삭제
    fun deleteItemListener(position: Int)

    // 링크 편집 페이지 이미지 선택
    fun selectImageListener(position: Int, imageUri: Uri)
}