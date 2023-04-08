package com.dwgu.linkive.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.ActivityFindPasswordBinding

class FindPasswordActivity : AppCompatActivity() {

    // viewBinding 선언
    lateinit var binding: ActivityFindPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()

        setOnClickListener()
    }

    private fun setOnClickListener() {
        // 인증 요청 버튼
        binding.btnRequestVerify.setOnClickListener {
            Log.d("msg", "인증 요청")
            Toast.makeText(this, "인증 요청이 전송되었습니다.", Toast.LENGTH_SHORT).show()
        }

        // 인증하기 버튼
        binding.btnVerify.setOnClickListener {
            Toast.makeText(this, "인증이 완료되었습니다.", Toast.LENGTH_SHORT).show()
        }

        // 취소 버튼
        binding.btnCheckFindPassword.setOnClickListener {
            // 로그인 화면으로 이동
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // 확인 버튼
        binding.btnCheckFindPassword.setOnClickListener {
            // 변경 내용 서버에 반영

            // 로그인 화면으로 이동
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}