package com.dwgu.linkive.EditLink.DragToMoveItem

// 드래그로 편집 아이템 이동을 위한 부분
interface ItemTouchHelperListener {
    fun onItemMove(from: Int, to: Int)
}