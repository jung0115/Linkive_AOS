package com.dwgu.linkive.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import com.dwgu.linkive.Login.loginRepository.Companion.api
import com.dwgu.linkive.Login.loginRepository.Companion.emailFlag
import com.dwgu.linkive.Login.loginRepository.Companion.mail
import com.dwgu.linkive.Login.loginRepository.Companion.serverVerifyNumber
import com.dwgu.linkive.Login.loginRepository.Companion.verifyNumber
import com.dwgu.linkive.Login.loginService.code
import com.dwgu.linkive.Login.loginService.email
import com.dwgu.linkive.Login.loginService.result
import com.dwgu.linkive.databinding.ActivityFindIdBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FindIdActivity : AppCompatActivity() {

    // viewBinding 선언
    lateinit var binding: ActivityFindIdBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityFindIdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 이메일 전송 메시지 숨기기
        binding.textSend.visibility = View.INVISIBLE

    }

    override fun onResume() {
        super.onResume()

        // 이메일 에러 이미지 숨기기
        binding.btnErrorEmail.visibility = View.GONE

        setOnClickListener()
    }

    private fun setOnClickListener() {

        // 닫기 버튼
        binding.btnCloseFindId.setOnClickListener {
            Log.d("msg", "아이디 찾기 닫기")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // 인증 요청
        binding.btnRequestVerify.setOnClickListener {

            // 이메일 중복 확인 api
            mail = binding.inputEmail.text.toString()
            postCheckIsEmail(mail)

        }

        // 인증하기
        binding.btnVerify.setOnClickListener {

            // 존재하는 이메일인 경우
            if(emailFlag) {

                verifyNumber = binding.inputVerifyNumber.text.toString()
                Log.d("find id - verify num", verifyNumber)

                // 인증번호가 서버와 일치하는 경우
                if(verifyNumber == serverVerifyNumber) {
                    // 이메일 인증 성공
                    Log.d("find  id - send num", "match")
                    Toast.makeText(this@FindIdActivity, "인증이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    binding.btnErrorEmail.visibility = View.GONE

                    // 사용자의 이메일로 아이디를 보내는 api
                    postFindId(mail)
                } else {
                    // 인증번호를 다시 확인해주세요.
                    Toast.makeText(this@FindIdActivity, "인증번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@FindIdActivity, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 이메일 중복 확인 api
    private fun postCheckIsEmail(mail: String){
        var data = email(mail)
        api.postCheckIsEmail(data).enqueue(object :Callback<result>{
            override fun onFailure(call: Call<result>, t: Throwable) {}

            override fun onResponse(call: Call<result>, response: Response<result>) {
                var code = response.code()
                // 존재하는 이메일인 경우
                if(code == 200) {
                    // 이메일 전송 메시지 보내기
                    binding.textSend.visibility = View.VISIBLE
                    emailFlag = true

                    // 3분 타이머 시작
                    startTimer()

                    //  이메일 인증 api 호출
//                    verifyNumber = binding.inputVerifyNumber.text.toString()
                    postSendVerifyEmailFindId(mail)
                }
                // 존재하지 않는 이메일인 경우
                else if(code == 400) {
                    Toast.makeText(this@FindIdActivity, "존재하지 않는 이메일입니다.", Toast.LENGTH_SHORT).show()
                    binding.btnErrorEmail.visibility = View.VISIBLE
                }
            }
        })
    }

    // 인증번호 일치 확인 api
    private fun postSendVerifyEmailFindId(mail: String) {
        var data = email(mail)
        api.postSendVerifyEmailFindId("findId", data).enqueue(object:Callback<code>{

            // 인증번호 전송에 실패하는 경우
            override fun onFailure(call: Call<code>, t: Throwable) {
                Log.d("find id - send verify number fail", t.toString())
                Toast.makeText(this@FindIdActivity, "서버 오류 입니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<code>, response: Response<code>) {

                serverVerifyNumber = response.body()?.code.toString()
                Log.d("find id - send number success", serverVerifyNumber)
            }
        })
    }

    // 사용자의 이메일로 아이디를 보내는 api
    private fun postFindId(text: String) {
        var data = email(text)
        api.postFindId(data).enqueue(object: Callback<result>{
            override fun onFailure(call: Call<result>, t: Throwable) {
                Log.d("find id - send id to mail fail", t.toString())
                Toast.makeText(this@FindIdActivity, "일시적인 오류입니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<result>, response: Response<result>) {
                Log.d("find id - send id mail success", response.message())

                // 로그인 화면으로 이동
                val intent = Intent(this@FindIdActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
    }

    // 3분 타이머
    private fun startTimer() {
        loginRepository.timer = object : CountDownTimer(loginRepository.totalTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = (millisUntilFinished / 1000).toInt()
                val minutes = seconds / 60
                val remainingSeconds = seconds % 60
                val timeString = String.format("%02d:%02d", minutes, remainingSeconds)
                binding.textTime.text = timeString
            }

            override fun onFinish() {
                stopTimer()
            }
        }

        loginRepository.timer.start()
    }

    // 타이머 멈추기
    private fun stopTimer() {
        loginRepository.timer.cancel()
        binding.textTime.visibility = View.GONE
        binding.btnRequestVerify.isEnabled = true
    }
}