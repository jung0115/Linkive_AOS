package com.dwgu.linkive.LinkView.MoveFolderRecycler

// 폴더 이동 아이템 데이터 타입
data class MoveFolderItem(
    val folderNum: Int,               // 폴더 번호
    val folderName: String,           // 폴더명
    var selectFolder: Boolean = false // 폴더 선택 유무
)
