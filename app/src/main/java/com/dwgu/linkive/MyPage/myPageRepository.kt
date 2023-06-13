package com.dwgu.linkive.MyPage

import com.dwgu.linkive.Api.ApiClient
import com.dwgu.linkive.MyPage.myPageService.MyPageInterface
import retrofit2.Retrofit

class myPageRepository {
    companion object{
        // ApiClient의 instance 불러오기
        val retrofit: Retrofit = ApiClient.getInstance()
        // Retrofit의 interface 구현
        val api: MyPageInterface = retrofit.create(MyPageInterface::class.java)


        // 토큰 값
        lateinit var accessToken: String
        lateinit var refreshToken: String

        // 변수들
        lateinit var profileImg: String
    }
}