package com.dwgu.linkive.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.ActivityFindIdBinding
import kotlin.concurrent.timer

class FindIdActivity : AppCompatActivity() {

    // viewBinding 선언
    lateinit var binding: ActivityFindIdBinding

    // 타이머 변수
    private var min = 3
    private var sec = 60

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindIdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 이메일 전송 메시지 숨기기
        binding.textSend.visibility = View.INVISIBLE
    }

    override fun onResume() {
        super.onResume()

        setOnClickListener()
    }

    private fun setOnClickListener() {
        // 닫기 버튼
        binding.btnCloseFindId.setOnClickListener {
            Log.d("msg", "아이디 찾기 닫기")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // 인증 요청
        binding.btnRequestVerify.setOnClickListener {

            // 서버에 요청
            Log.d("msg", "이메일 전송")

            // 이메일 전송 메시지 보내기
            binding.textSend.visibility = View.VISIBLE

            // 3분 타이머 시작
            startTimer()
        }

        // 인증하기
        binding.btnVerify.setOnClickListener {
            // 서버에 확인
            // 알림
            Toast.makeText(this, "인증되었습니다.", Toast.LENGTH_SHORT).show()

            // 존재하는 이메일이면, 해당 이메일로 아이디 전송
        }

    }

    // 3분 타이머 시작
    private fun startTimer() {
        min--
        sec--
        // 1000 = 1초
        timer(period = 1000,) {
            runOnUiThread {
                if(sec<10) {
                    binding.textTime.text = "0$min:0$sec"
                } else {
                    binding.textTime.text = "0$min:$sec"
                }
                // 3분 후에, 타이머 중지
                if(sec==0 && min==0) cancel()
                else if(sec == 0) {
                    min--
                    sec = 59
                }
                sec--
            }
        }
    }
}