package com.dwgu.linkive.Login

import android.app.Application
import com.dwgu.linkive.Login.loginService.PreferenceUtil
import com.dwgu.linkive.R
import com.kakao.sdk.common.KakaoSdk

class GloabalApplication : Application(){

    companion object {
        lateinit var prefs: PreferenceUtil
    }
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "bd6da9b30b3570af0f0b3749be17b5cb")
        prefs = PreferenceUtil(applicationContext)
    }
}