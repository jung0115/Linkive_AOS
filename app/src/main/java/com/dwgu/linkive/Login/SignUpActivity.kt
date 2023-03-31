package com.dwgu.linkive.Login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.ActivitySignUpBinding
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {

    // Viewbinding
    private lateinit var binding: ActivitySignUpBinding

    // 데이터 저장
    lateinit var nickName: String
    lateinit var email: String
    lateinit var id: String
    lateinit var password: String
    lateinit var checkPassword: String

    private var idFlag = false
    private var passwordFlag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()

        binding.btnErrorPassword.visibility = View.GONE
        binding.btnErrorCheckPassword.visibility = View.GONE

        setOnClickListener()
    }

    private fun setOnClickListener() {
        // 인증 요청 버튼 클릭 시
        binding.btnRequestVerify.setOnClickListener {
            // 서버에 이메일 인증 요청 보내기, 이메일이 유효한 경우
            Toast.makeText(this@SignUpActivity, "입력한 이메일로 인증 메일이 전송되었습니다.", Toast.LENGTH_SHORT).show()
        }
        // 인증하기 버튼 클릭 시
        binding.btnVerify.setOnClickListener {
            // 인증 확인 하기
        }
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
        // 회원가입 버튼 클릭 시
        binding.btnSignUp.setOnClickListener {
            // 적절한 닉네임, 이메일, 아이디, 비밀번호이며 개인 정보 제공에 동의한 경우(flag로 계산)
            // 회원가입 완료 및 메인 화면으로 이동
            nickName = binding.inputNickname.text.toString()
            email = binding.inputEmail.text.toString()
            id = binding.inputId.text.toString()
            password = binding.inputPassword.text.toString()
            checkPassword = binding.inputCheckPassword.text.toString()
        }
        // 로그인 버튼 클릭 시
        binding.btnLogin.setOnClickListener {
            // 로그인 화면으로 이동
        }
    }

    // 특수 문자 존재 여부를 확인하는 메서드
    private fun hasSpecialCharacter(string: String): Boolean {
        for(i in string.indices) {
            if (!Character.isLetterOrDigit(string[i])) {
                return true
            }
        }
        return true
    }

    // 영문자 존재 여부를 확인하는 메서드
    private fun hasAlphabet(string: String): Boolean {
        for(i in string.indices) {
            if (!Character.isAlphabetic(string[i].code)) {
                return true
            }
        }
        return false
    }

    // id 정규식을 확인하는 메서드
    fun idRegex(id: String): Boolean {
        if((!hasSpecialCharacter(id)) and (hasAlphabet(id))) {
            return true
        }
        return false
    }

    // password 정규식을 확인하는 메서드
    fun passwordRegex(password: String): Boolean {
        return password.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&.])[A-Za-z[0-9]$@$!%*#?&.]{8,16}$".toRegex())
    }

    private val idListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            if (s != null) {
                when {
                    s.isEmpty() -> {
                        binding.inputId.error = "아이디를 입력해주세요."
                        idFlag = false
                    }
                    !idRegex(s.toString()) -> {
                        binding.inputId.error = "아이디 양식이 맞지 않습니다"
                        idFlag = false
                    }
                    else -> {
                        binding.inputId.error = null
                        idFlag = true
                    }
                }
                flagCheck()
            }
        }
    }

    private val passwordListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            if (s != null) {
                when {
                    s.isEmpty() -> {
                        binding.inputPassword.error = "비밀번호를 입력해주세요."
                        passwordFlag = false
                    }
                    !passwordRegex(s.toString()) -> {
                        binding.inputPassword.error = "비밀번호 양식이 일치하지 않습니다."
                        passwordFlag = false
                    }
                    else -> {
                        binding.inputPassword.error = null
                        passwordFlag = true
                    }
                }
                flagCheck()
            }
        }
    }

    fun flagCheck() {
        binding.btnSignUp.isEnabled = idFlag && passwordFlag
    }
}