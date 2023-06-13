package com.dwgu.linkive.EditLink.EditLinkOption

import android.net.Uri
import com.dwgu.linkive.EditLink.EditLinkRecyclerview.EditLinkPlaceItem

public interface EditLinkOptionListener {

    // 링크 편집 페이지에서 아이템 삭제
    fun deleteItemListener(position: Int)

    // 링크 편집 페이지 이미지 선택
    fun selectImageListener(position: Int, imageUri: Uri)

    // 링크 편집 페이지 글, 코드, 링크, 할 일 내용 리셋
    fun resetItemListener(position: Int, itemType: String)

    // 링크 편집 페이지 > 링크 아이템 내용 적용 (url에서 가져온 내용)
    fun setLinkItemListener(position: Int, linkTile: String?, linkUrl: String?)

    // 링크 편집 페이지 > 장소 아이템 내용 적용 (카카오 api에서 가져온 내용)
    fun setPlaceItemListener(placeItem: EditLinkPlaceItem)
}