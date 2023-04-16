package com.dwgu.linkive.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.ActivityFindPasswordBinding
import kotlin.concurrent.timer

class FindPasswordActivity : AppCompatActivity() {

    // viewBinding 선언
    lateinit var binding: ActivityFindPasswordBinding

    // 타이머 변수
    private var min = 3
    private var sec = 60

    // flag
    private var idFlag = false
    private var emailFlag = false
    private var numberFlag = false
    private var passwordFlag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()

        // 아이디, 이메일, 비밀번호 경고 아이콘 숨기기
        binding.btnErrorId.visibility = View.GONE
        binding.btnErrorPassword.visibility = View.GONE
        binding.btnErrorEmail.visibility = View.GONE
        binding.btnErrorCheckPassword.visibility = View.GONE

        setOnClickListener()
    }

    private fun setOnClickListener() {

        // 확인 버튼
        binding.btnIdVerify.setOnClickListener {
            // 서버와 통신 후, 존재하는 아이디인지 확인
            // 존재하는 경우
            if(idFlag) {
                Toast.makeText(this, "존재하는 아이디입니다.", Toast.LENGTH_SHORT).show()
                binding.btnErrorId.visibility = View.GONE
            } else {
                Toast.makeText(this, "존재하지 않는 아이디입니다.", Toast.LENGTH_SHORT).show()
                binding.btnErrorId.visibility = View.VISIBLE
            }
        }


        // 인증 요청 버튼
        binding.btnRequestVerify.setOnClickListener {
            Log.d("msg", "인증 요청")
            // 존재하는 이메일인 경우
            if(emailFlag) {
                Toast.makeText(this, "인증 요청이 전송되었습니다.", Toast.LENGTH_SHORT).show()
                binding.btnErrorEmail.visibility = View.GONE
                // 타이머 작동
                startTimer()
            } else {
                Toast.makeText(this, "존재하지 않는 이메일입니다.", Toast.LENGTH_SHORT).show()
                binding.btnErrorEmail.visibility = View.VISIBLE
            }

        }

        // 인증하기 버튼
        binding.btnVerify.setOnClickListener {
            // 30초 이내에 인증하는 경우 + 인증 번호가 맞는 경우
            if(numberFlag) {
                Toast.makeText(this, "인증이 완료되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                // 인증시간 초과한 경우
                Toast.makeText(this, "인증시간이 초과되었습니다.", Toast.LENGTH_SHORT).show()
                // 잘못된 번호를 입력한 경우
                Toast.makeText(this, "인증번호가 틀렸습니다. 인증 요청을 다시 눌러주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        // 비밀번호 형식 확인
        binding.inputPassword.addTextChangedListener(passwordListener)

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
        binding.btnViewHide2.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked) {
                // 비밀번호를 보이게
                binding.inputCheckPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.btnViewHide2.setBackgroundResource(R.drawable.view)
            } else {
                // 비밀번호가 안보이게
                binding.inputCheckPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.btnViewHide2.setBackgroundResource(R.drawable.hidden)
            }
            // 커서가 항상 뒤에 위치하도록
            binding.inputCheckPassword.setSelection(binding.inputCheckPassword.length())
        }

        // 비밀번호 일치 확인
        binding.inputCheckPassword.addTextChangedListener(passwordMatchListener)

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

    // password 정규식
    fun passwordRegex(password: String): Boolean {
        // 대소문자, 숫자, 특수문자, 8~16자  $@!%*#?&.
        return password.matches("[a-zA-Z[0-9][$@!%*#?&.]]{8,16}".toRegex())
    }

    private val passwordListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(s: Editable?) {
            Log.d("msg pass", s.toString())
            if (s != null) {
                when {
                    // 비밀번호 미입력시
                    s.isEmpty() -> {
                        Toast.makeText(this@FindPasswordActivity, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                        binding.btnErrorPassword.visibility = View.VISIBLE
                        binding.btnViewHide.visibility = View.GONE
                    }
                    // 비밀번호 정규식에 맞지 않는 경우
                    !passwordRegex(s.toString()) -> {
                        Log.d("msg", passwordRegex(s.toString()).toString())
                        Toast.makeText(this@FindPasswordActivity, "비밀번호는 8~16자 이내의 대소문자, 숫자, '!, @, #, $, &, *, .' 조합으로 입력해주세요.", Toast.LENGTH_SHORT).show()
                        binding.btnErrorPassword.visibility = View.VISIBLE
                        binding.btnViewHide.visibility = View.GONE
                    }
                    else -> {
                        binding.btnErrorPassword.visibility = View.GONE
                        binding.btnViewHide.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private val passwordMatchListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(s: Editable?) {
            // 비밀번호가 일치하지 않을 때
            if(binding.inputCheckPassword.text != binding.inputPassword.text) {
                Toast.makeText(this@FindPasswordActivity, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                binding.btnErrorCheckPassword.visibility = View.VISIBLE
            } else {
                binding.btnErrorCheckPassword.visibility = View.GONE
                passwordFlag = true
            }
        }
    }
}