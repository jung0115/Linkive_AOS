package com.dwgu.linkive.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.dwgu.linkive.Login.loginRepository.Companion.api
import com.dwgu.linkive.Login.loginRepository.Companion.checkPassword
import com.dwgu.linkive.Login.loginRepository.Companion.emailFlag
import com.dwgu.linkive.Login.loginRepository.Companion.flagCheck
import com.dwgu.linkive.Login.loginRepository.Companion.id
import com.dwgu.linkive.Login.loginRepository.Companion.idFlag
import com.dwgu.linkive.Login.loginRepository.Companion.idRegex
import com.dwgu.linkive.Login.loginRepository.Companion.mail
import com.dwgu.linkive.Login.loginRepository.Companion.nickName
import com.dwgu.linkive.Login.loginRepository.Companion.nickNameFlag
import com.dwgu.linkive.Login.loginRepository.Companion.nickNameRegex
import com.dwgu.linkive.Login.loginRepository.Companion.password
import com.dwgu.linkive.Login.loginRepository.Companion.passwordFlag
import com.dwgu.linkive.Login.loginRepository.Companion.serverVerifyNumber
import com.dwgu.linkive.Login.loginRepository.Companion.verifyNumber
import com.dwgu.linkive.Login.loginService.code
import com.dwgu.linkive.Login.loginService.email
import com.dwgu.linkive.Login.loginService.newId
import com.dwgu.linkive.Login.loginService.result
import com.dwgu.linkive.Login.loginService.signUp
import com.dwgu.linkive.MainActivity
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.ActivitySignUpBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignUpActivity : AppCompatActivity() {

    // Viewbinding
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // viewBinding
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()

        //  에러 이미지 숨기기
        binding.btnErrorPassword.visibility = View.GONE
        binding.btnErrorCheckPassword.visibility = View.GONE
        binding.btnErrorNickname.visibility = View.GONE
        binding.btnErrorEmail.visibility = View.GONE
        binding.btnErrorId.visibility = View.GONE

        // 닉네임, 아이디, 비밀번호 정규식 확인
        changedListeners()

        setOnClickListener()
    }

    private fun setOnClickListener() {

        // 인증 요청 버튼 클릭 시
        binding.btnRequestVerify.setOnClickListener {

            // 이메일 중복 검사 api 호출
            mail = binding.inputEmail.text.toString()
            postCheckIsEmail(mail)

        }

        // 인증하기 버튼 클릭 시
        binding.btnVerify.setOnClickListener {

            // 인증번호가 서버와 일치하는 경우
            if(serverVerifyNumber == verifyNumber) {
                Log.d("sign up - send num", "match")
                Toast.makeText(this@SignUpActivity, "인증이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                binding.btnErrorEmail.visibility = View.GONE
            } else {
                // 인증번호를 다시 확인해주세요.
                Toast.makeText(this@SignUpActivity, "인증번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
            }

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

            // 닉네임 중복 확인 api

            // 아이디 중복 확인 api
            id = binding.inputId.text.toString()
            postCheckNewId(id)

            // 비밀번호 일치 여부 확인
            password = binding.inputPassword.text.toString()
            checkPassword = binding.inputCheckPassword.text.toString()
            matchPassword(password, checkPassword)

            // 모든 항목을 올바르게 작성한 경우 - 홈 화면으로 이동, 서버에 회원 정보 update
            if(flagCheck()) {

                nickName = binding.inputNickname.text.toString()
                mail = binding.inputEmail.text.toString()
                id = binding.inputId.text.toString()
                password = binding.inputPassword.text.toString()

                // 회원가입 api 호출
                postSignUp(id, password, mail, nickName)

            } else {

                Toast.makeText(this, "모든 항목을 작성해주세요.", Toast.LENGTH_SHORT).show()

            }
        }

        // 로그인 버튼 클릭 시
        binding.btnLogin.setOnClickListener {
            // 로그인 화면으로 이동
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun changedListeners() {
        // 닉네임 정규식 확인 - nickNameListener 호출
        binding.inputNickname.addTextChangedListener(nickNameListener)

        // 아이디 정규식 확인 - idListener 호출
        binding.inputId.addTextChangedListener(idListener)

        // 비밀번호 정규식 확인 - passwordListener 호출
        binding.inputPassword.addTextChangedListener(passwordListener)

        //  비밀번호 일치  확인 -  passwordMatchListener 호출
//        binding.inputCheckPassword.addTextChangedListener(passwordMatchListener)
    }

    private val nickNameListener = object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            if (s != null) {
                when {
                    // 닉네임 칸이 공백일 경우
                    s.isEmpty() -> {
                        Log.d("error", "nickname empty")
                        binding.btnErrorNickname.visibility = View.VISIBLE
                        Toast.makeText(this@SignUpActivity, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show()
                    }
                    // 닉네임 정규식에 맞지 않는 경우
                    !nickNameRegex(s.toString()) -> {
                        Log.d("error", "nick name regex false")
                        binding.btnErrorNickname.visibility = View.VISIBLE
                        Toast.makeText(this@SignUpActivity, "10자 이내의 한글, 영문자, 숫자 조합으로 입력해주세요.", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        binding.btnErrorNickname.visibility = View.GONE
                        nickNameFlag = true
                    }
                }
            }
        }
    }

    val idListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(s: Editable?) {
            if (s != null) {
                when {
                    // 아이디 미입력시
                    s.isEmpty() -> {
                        Log.d("error", "id empty")
                        binding.btnErrorId.visibility = View.VISIBLE
                        Toast.makeText(this@SignUpActivity, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    }
                    // 아이디 정규식에 맞지 않는 경우
                    !idRegex(s.toString()) -> {
                        Log.d("error", "id regex 위배")
                        binding.btnErrorId.visibility = View.VISIBLE
                        Toast.makeText(this@SignUpActivity, "아이디는 14자 이내의 소문자, 숫자, 특수문자 '_' 조합으로 입력해주세요..", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        binding.btnErrorId.visibility = View.GONE
                        idFlag = true
                    }
                }
            }
        }
    }

    private val passwordListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(s: Editable?) {
            if (s != null) {
//                passwordRegex(s.toString())
                when {
                    // 비밀번호 미입력시
                    s.isEmpty() -> {
                        Toast.makeText(this@SignUpActivity, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                        binding.btnErrorPassword.visibility = View.VISIBLE
                        binding.btnViewHide.visibility = View.GONE
                    }
                    // 비밀번호 정규식에 맞지 않는 경우
//                    !passwordRegex(s.toString()) -> {
                    !idRegex(s.toString()) -> {
                        Toast.makeText(this@SignUpActivity, "비밀번호는 8~16자 이내의 대소문자, 숫자, '!, @, #, $, &, *, .' 조합으로 입력해주세요.", Toast.LENGTH_SHORT).show()
//                        binding.btnErrorPassword.visibility = View.VISIBLE
//                        binding.btnViewHide.visibility = View.GONE
                    }
                    else -> {
                        binding.btnErrorPassword.visibility = View.GONE
                        binding.btnViewHide.visibility = View.VISIBLE
                        passwordFlag = true
                    }
                }
            }
        }
    }

    val passwordMatchListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(s: Editable?) {

            password = binding.inputPassword.text.toString()
            checkPassword = binding.inputCheckPassword.text.toString()

            if(password  == checkPassword) {
                // 비밀번호가 일치할 때
                Log.d("sign up", "match password")
                binding.btnErrorCheckPassword.visibility = View.GONE
                passwordFlag = true
            } else {
                Log.d("sign up", "unmatch")
                Toast.makeText(this@SignUpActivity, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                binding.btnErrorCheckPassword.visibility = View.VISIBLE
            }
        }
    }

    // 비밀번호 일치 여부 확인 함수
    private fun matchPassword(password: String, checkPassword: String) {
        if(password == checkPassword) {
            // 비밀번호가 일치할 때
            Log.d("sign up", "match password")
            binding.btnErrorCheckPassword.visibility = View.GONE
            passwordFlag = true
        } else {
            Log.d("sign up", "unmatch")
            Toast.makeText(this@SignUpActivity, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            binding.btnErrorCheckPassword.visibility = View.VISIBLE
            passwordFlag = false
        }
    }

    // 이메일 중복 확인 api
    private fun postCheckIsEmail(mail: String) {
        var data = email(mail)
        Toast.makeText(this@SignUpActivity, data.email, Toast.LENGTH_SHORT).show()
        api.postCheckIsEmail(data).enqueue(object:Callback<result>{
            override fun onFailure(call: Call<result>, t: Throwable) {}

            override fun onResponse(call: Call<result>, response: Response<result>) {

                //  코드로 유효한 이메일인지 검증
                var code = response.code()

                // 이메일이 중복되는 경우 - 200
                if(code == 200) {
                    binding.btnErrorEmail.visibility = View.VISIBLE
                    Toast.makeText(this@SignUpActivity, "중복된 이메일입니다.", Toast.LENGTH_SHORT).show()
                    Log.d("sign up", "duplicated email")
                }
                // 이메일이 중복되지 않는 경우 - 400
                else if(code == 400) {
                    Log.d("sign up", "vaild email")
                    Toast.makeText(this@SignUpActivity, "사용가능한 이메일입니다.", Toast.LENGTH_SHORT).show()

                    // 올바른 이메일 표시
                    emailFlag = true

                    // 3분 타이머 시작
                    startTimer()

                    //  이메일 인증 api 호출
                    verifyNumber = binding.inputVerifyNumber.text.toString()
                    postSendVerifyEmail(verifyNumber)
                }
            }
        })
    }

    // 인증번호 일치 확인 api
    private fun postSendVerifyEmail(num: String) {
        var data = email(num)
        api.postSendVerifyEmail("create", data).enqueue(object:Callback<code>{

            // 인증번호 전송에 실패하는 경우
            override fun onFailure(call: Call<code>, t: Throwable) {
                Log.d("sign up - send verify number fail", t.toString())
                Toast.makeText(this@SignUpActivity, "서버 오류 입니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            }

            // 인증번호 전송에 성공하는 경우
            override fun onResponse(call: Call<code>, response: Response<code>) {

                serverVerifyNumber = response.code().toString()
                Log.d("sign up - send number success", serverVerifyNumber)
                Log.d("sign up - verify num", num)
            }
        })
    }

    // 아이디 중복 확인 api
    private fun postCheckNewId(id: String) {
        var data = newId(id)
        api.postCheckNewId(data).enqueue(object :Callback<result>{
            override fun onFailure(call: Call<result>, t: Throwable) {}

            override fun onResponse(call: Call<result>, response: Response<result>) {
                var code = response.code()
                Log.d("sign up - code", code.toString())
                // 서버에 존재하지 않는 아이디
                if(code == 200) {
                    Log.d("sign up", "id not exists")

                    // 올바른 아이디 표시
                    idFlag = true
                }
                // 서버에 존재하는 아이디
                else if(code == 409) {
                    Log.d("sign up", "id exists")
                    Toast.makeText(this@SignUpActivity, "중복된 아이디 입니다.", Toast.LENGTH_SHORT).show()
                }
                else {
                    Log.d("sign up", "other error")
                    Toast.makeText(this@SignUpActivity, "일시적인 오류입니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    // 회원가입 api
    private fun postSignUp(id: String, password: String, email: String, nickName: String) {
        var data = signUp(id, password, email, nickName)
        api.postSignUp(data).enqueue(object:Callback<result>{

            override fun onFailure(call: Call<result>, t: Throwable) {
                Log.d("sign up - fail", t.toString())
                Toast.makeText(this@SignUpActivity, "일시적인 오류입니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<result>, response: Response<result>) {
                Log.d("sign up - success", response.message())

                //  메인 화면으로 이동
                Toast.makeText(this@SignUpActivity, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@SignUpActivity, MainActivity::class.java)
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