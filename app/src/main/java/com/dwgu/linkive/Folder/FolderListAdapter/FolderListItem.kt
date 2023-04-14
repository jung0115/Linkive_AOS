package com.dwgu.linkive.Folder.FolderListAdapter

import com.dwgu.linkive.R
import java.time.LocalDateTime
import java.util.Date

data class FolderListItem(
    val name: String,
    var cover: Int = R.drawable.img_folder_background_blue,
    var isLock: Boolean = false,
    val createdDate: LocalDateTime
)