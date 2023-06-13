package com.dwgu.linkive.LinkMemoApi.GetAllPageSheet

import com.google.gson.annotations.SerializedName

// 페이지시트 전체 조회 데이터
data class GetPageSheetListData(
    @SerializedName("pagesheets") // 페이지시트 list
    val pagesheets: MutableList<GetPageSheetData>,
)

data class GetPageSheetData(
    @SerializedName("pagesheet_num") // 페이지시트 번호
    val pagesheet_num: Int,

    @SerializedName("name") // 페이지시트 이름
    val name: String,

    @SerializedName("layout") // 페이지시트 내용
    val layout: MutableList<String>,
)