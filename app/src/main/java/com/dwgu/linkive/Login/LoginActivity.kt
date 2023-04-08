package com.dwgu.linkive.Login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import com.dwgu.linkive.Home.HomeFragment
import com.dwgu.linkive.MainActivity
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.ActivityLoginBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient

class LoginActivity : AppCompatActivity() {

    // ViewBinding
    lateinit var binding: ActivityLoginBinding

    // 변수들
    private var id = "11"
    private var password = "12"
    lateinit var inputId: String
    lateinit var inputPassword: String
    private var vaildLogin = true

    lateinit var kakaoCallback: (OAuthToken?, Throwable?) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    override fun onResume() {
        super.onResume()

        binding.btnErrorId.visibility = View.GONE
        binding.btnErrorPassword.visibility = View.GONE

        setOnClickListener()
    }

    private fun setOnClickListener() {

        // 비밀번호 보여주기, 숨기기 버튼
        binding.btnViewHide.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked) {
                // 비밀번호를 보이게
                binding.inputPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.btnViewHide.setBackgroundResource(R.drawable.view)
            } else {
                // 비밀번호가 안보이게
                binding.inputPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.btnViewHide.setBackgroundResource(R.drawable.hidden)
            }
            // 커서가 항상 뒤에 위치하도록
            binding.inputPassword.setSelection(binding.inputPassword.length())
        }

        // 로그인 버튼 클릭 시
        binding.btnLogin.setOnClickListener {
            // 아이디, 비밀번호 입력받기
            inputId = binding.inputId.text.toString()
            inputPassword = binding.inputPassword.text.toString()

            // 아이디, 비밀번호가 일치하는 경우
            if (id == inputId && password == inputPassword){
                Log.d("msg", "login success")
                binding.btnErrorId.visibility = View.GONE
                binding.btnErrorPassword.visibility = View.GONE

                // 메인 화면으로 이동

            } else {
                // 일치하지 않는 경우
                Log.d("msg", "login fail")
                if(id != inputId) {
                    // 아이디 불일치
                    binding.btnErrorId.visibility = View.VISIBLE
                } else if(password != inputPassword) {
                    // 비밀번호 불일치
                    binding.btnErrorPassword.visibility = View.VISIBLE
                    binding.btnViewHide.visibility = View.GONE
                } else if(id != inputId && password != inputPassword){
                    // 둘 다 불일치
                    binding.btnErrorId.visibility = View.VISIBLE
                    binding.btnErrorPassword.visibility = View.VISIBLE
                    binding.btnViewHide.visibility = View.GONE
                }
            }
        }

        // 아이디/비밀번호 찾기 버튼 클릭 시, 해당 화면으로 이동
        binding.btnFindId.setOnClickListener {
            Log.d("msg", "find id work")
            val intent = Intent(this@LoginActivity, FindIdActivity::class.java)
            startActivity(intent)
        }
        binding.btnFindPassword.setOnClickListener {
            Log.d("msg", "find password work")
            val intent = Intent(this@LoginActivity, FindPasswordActivity::class.java)
            startActivity(intent)
        }

        // 회원 가입 버튼 클릭 시, 회원 가입 화면으로 이동
        binding.btnSignUp.setOnClickListener {
            Log.d("msg", "sign up work")
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

        // 로그인 버튼 클릭 시,
        // 유효한 로그인이면, 메인 화면으로 이동
        binding.btnLogin.setOnClickListener {
            if(vaildLogin) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "아이디나 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 소셜 로그인
        binding.btnKakaoLogin.setOnClickListener {
            Log.d("msg", "kakao login work")

            // hash 값 받아오기, kakao login 정보 확인
            checkkaKaoLogin()
            // 로그인 성공/실패 시, 콜백
            setKakaoCallback()

            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if(UserApiClient.instance.isKakaoTalkLoginAvailable(this@LoginActivity)){
                UserApiClient.instance.loginWithKakaoTalk(this@LoginActivity, callback = kakaoCallback)
            }else{
                UserApiClient.instance.loginWithKakaoAccount(this@LoginActivity, callback = kakaoCallback)
            }
        }
        binding.btnNaverLogin.setOnClickListener {
            Log.d("msg", "naver login work")
        }
        binding.btnGoogleLogin.setOnClickListener {
            Log.d("msg", "google login work")
        }
    }

    fun checkkaKaoLogin() {
        // Kakao Login Hash 값 받아오기
        var keyHash = Utility.getKeyHash(this)
        Log.d("keyHash", keyHash)

        // Kakao Login 정보 확인
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Toast.makeText(this, "토큰 정보 보기 실패", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            else if (tokenInfo != null) {
                Toast.makeText(this, "토큰 정보 보기 성공", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
        }
    }

    fun setKakaoCallback() {
        kakaoCallback = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                        Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                        Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                        Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                        Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                        Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                        Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.ServerError.toString() -> {
                        Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                        Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                    }
                    else -> { // Unknown
                        Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else if (token != null) {
                Toast.makeText(this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
        }
    }
}