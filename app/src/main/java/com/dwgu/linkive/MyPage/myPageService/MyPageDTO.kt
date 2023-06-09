package com.dwgu.linkive.MyPage.myPageService

import com.dwgu.linkive.Login.loginService.Infos
import com.google.gson.annotations.SerializedName

data class result(
    @SerializedName("message") var result: String
)

data class profileImg(
    @SerializedName("profileImg") var profileImg: String
)

data class userInfo(
    @SerializedName("userInfo") var userInfo: Infos
)

data class currentPw(
    @SerializedName("currentPassword") var currentPassword: String
)

data class changeInfoPw(
    @SerializedName("newNickname") var newNickname: String,
    @SerializedName("newId") var newId: String,
    @SerializedName("newPassword") var newPassword: String,
    @SerializedName("newProfileImg") var newProfileImg: String
)

data class changeInfo(
    @SerializedName("newNickname") var newNickname: String,
    @SerializedName("newId") var newId: String,
    @SerializedName("newProfileImg") var newProfileImg: String
)

data class emails(
    @SerializedName("email") var email: String,
    @SerializedName("password") var password: String
)

data class email(
    @SerializedName("email") var email: String
)

data class newId(
    @SerializedName("newId") var newId: String
)

data class pageSheetCreate(
    @SerializedName("name") var name: String,
    @SerializedName("layout") var layout: String
)

data class pageSheetModify(
    @SerializedName("pagesheet_num") var num: Int,
    @SerializedName("name") var name: String,
    @SerializedName("layout") var layout: String
)

data class pageSheetNum(
    @SerializedName("pagesheet_num") var num: Int
)