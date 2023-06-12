package com.dwgu.linkive.ImageUploadApi

import com.google.gson.annotations.SerializedName

data class ImageUploadResponse(
    @SerializedName("file_info")
    val file_info: ImageFileInfo,
)

data class ImageFileInfo(
    @SerializedName("fieldname")    // 입력 form 필드 이름
    val fieldname: String,

    @SerializedName("originalname") // 이미지 파일의 원래 이름
    val originalname: String,

    @SerializedName("encoding")     // 이미지 파일의 인코딩 종류
    val encoding: String,

    @SerializedName("mimetype")     // 업로드된 파일의 타입
    val mimetype: String,

    @SerializedName("destination")  // 파일이 업로드된 경로
    val destination: String,

    @SerializedName("filename")     // 이미지 파일이 서버에 저장된 실제 이름
    val filename: String,

    @SerializedName("path")         // 이미지 파일의 상대 경로
    val path: String,

    @SerializedName("size")         // 이미지 파일의 크기
    val size: String,
)

data class SetImage(
    var position: Int,
    var imageUrl: String
)