package com.dwgu.linkive.LinkView.SelectPagesheetRecycler

// PageSheet 선택 아이템
data class SelectPagesheetItem(
    val pagesheetNum: Int,
    val pagesheetName: String,
    var selectPagesheet: Boolean = false
)
