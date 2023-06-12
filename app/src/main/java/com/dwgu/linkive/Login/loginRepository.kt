package com.dwgu.linkive.Login

import android.os.CountDownTimer
import com.dwgu.linkive.Api.ApiClient
import com.dwgu.linkive.Login.loginService.LoginInterface
import retrofit2.Retrofit

class loginRepository {
    companion object {
        // ApiClient의 instance 불러오기
        val retrofit: Retrofit = ApiClient.getInstance()
        // Retrofit의 interface 구현
        val api: LoginInterface = retrofit.create(LoginInterface::class.java)

        // 변수들

        lateinit var nickName: String
        lateinit var mail: String
        lateinit var id: String
        lateinit var verifyNumber: String
        lateinit var password: String
        lateinit var checkPassword: String
        lateinit var serverVerifyNumber: String

        // 닉네임, id, password, email, 개인정보제공 작성 및 동의 여부 확인
        var nickNameFlag = false
        var idFlag = false
        var passwordFlag = false
        var emailFlag = false
        var checkAgreeFlag= false
        var numFlag = false

        // 타이머 변수
        var min = 3
        var sec = 60
        lateinit var timer: CountDownTimer
        val totalTime = 180000L // 3분 (밀리초 단위)

        // 닉네임 정규식
        // 대소문자, 숫자, 한글, 1~10자
        fun nickNameRegex(nickname: String): Boolean {
            val regex = Regex("^[가-힣a-zA-Z0-9]{1,10}$")
            return regex.matches(nickname)
        }

        // id 정규식
        // 소문자, 숫자, _, 1~14글자 만 가능
        fun idRegex(id: String): Boolean {
            val regex = Regex("^[a-z0-9_]{1,14}$")
            return regex.matches(id)
        }

        // password 정규식
        // 대소문자, 숫자, 특수문자, 8~16자  $@!%*#?&.
        fun passwordRegex(password: String): Boolean {
//            val regex = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*|?])[A-Za-z0-9!@#$%^&*|?]{8,16}$")
            val regex = Regex("^[a-zA-Z0-9_$@!%*#?&.]{8,16}$")
            return regex.matches(password)

        }

        fun flagCheck(): Boolean {
        return nickNameFlag && emailFlag && idFlag && passwordFlag && checkAgreeFlag
            // test 용
//            return false
        }
    }
}