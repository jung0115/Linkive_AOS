package com.dwgu.linkive.Login.loginService

import com.google.android.material.appbar.AppBarLayout.LayoutParams.ScrollEffect
import com.google.gson.annotations.SerializedName

data class login(
    @SerializedName("id") var id: String,
    @SerializedName("password") var password: String
)

data class loginTokens(
    @SerializedName("refreshToken") var refreshToken: String,
    @SerializedName("accessToken") var accessToken: String
)

data class signUp(
    @SerializedName("id") var id: String,
    @SerializedName("password") var password: String,
    @SerializedName("email") var email: String,
    @SerializedName("nickname") var nickname: String
)

data class result(
    @SerializedName("message") var result: String
)

data class emails(
    @SerializedName("email") var email: String,
    @SerializedName("password") var password: String
)

data class email(
    @SerializedName("email") var email: String
)

data class code(
    @SerializedName("verificationCode") var code: Int
)

data class findPw(
    @SerializedName("email") var email: String,
    @SerializedName("id") var id: String
)

data class newPw(
    @SerializedName("id") var id: String,
    @SerializedName("newPassword") var pw: String
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

data class newId(
    @SerializedName("newId") var newId: String
)

data class currentPw(
    @SerializedName("currentPassword") var currentPassword: String
)

data class profileImg(
    @SerializedName("profileImg") var profileImg: String
)

data class userInfo(
    @SerializedName("userInfo") var userInfo: Infos
)

data class Infos(
    @SerializedName("id") var id: String,
    @SerializedName("nickname") var nickname: String,
    @SerializedName("email") var email: String,
    @SerializedName("profile_img_url") var profileImgUrl: String,
    @SerializedName("socialLogin") var socialLogin: Boolean
)