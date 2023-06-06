package com.dwgu.linkive.LinkView.LinkViewMenuListener

// 링크 세부 페이지 메뉴
interface LinkViewMenuListener {
    fun backStackListener() // 이전 페이지 이동

    fun reopenLinkViewListener(memoNum: Int) // 링크 메모 편집 후에 링크 세부 페이지 다시 접속
}