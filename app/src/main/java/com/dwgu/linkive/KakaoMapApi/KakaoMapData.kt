package com.dwgu.linkive.KakaoMapApi

import com.google.gson.annotations.SerializedName

data class KakaoMapData(
    @SerializedName("documents")
    val documents: MutableList<kakaoMapDocuments>,

    @SerializedName("meta")
    val meta: KakaoMapMeta,
)

data class kakaoMapDocuments(
    @SerializedName("address_name") // 지번 주소
    val address_name: String,

    @SerializedName("category_group_code")
    val category_group_code: String,

    @SerializedName("category_group_name")
    val category_group_name: String,

    @SerializedName("category_name")
    val category_name: String,

    @SerializedName("distance")
    val distance: String,

    @SerializedName("id")
    val id: String,

    @SerializedName("phone")
    val phone: String,

    @SerializedName("place_name")  // 장소명
    val place_name: String,

    @SerializedName("place_url")
    val place_url: String,

    @SerializedName("road_address_name") // 도로명 주소
    val road_address_name: String,

    @SerializedName("x")
    val x: String,

    @SerializedName("y")
    val y: String,
)

data class KakaoMapMeta(
    @SerializedName("is_end")
    val is_end: Boolean,

    @SerializedName("pageable_count")
    val pageable_count: Int,

    @SerializedName("same_name")
    val same_name: KakaoMapSameName,

    @SerializedName("total_coun")
    val total_count: Int,
)

data class KakaoMapSameName(
    @SerializedName("keyword")
    val keyword: String,

    @SerializedName("region")
    val region: MutableList<String>,

    @SerializedName("selected_region")
    val selected_region: String,
)