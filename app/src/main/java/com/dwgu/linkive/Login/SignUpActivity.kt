package com.dwgu.linkive.Login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.dwgu.linkive.Api.ApiClient
import com.dwgu.linkive.Login.loginService.LoginInterface
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
import retrofit2.Retrofit
import kotlin.concurrent.timer

class SignUpActivity : AppCompatActivity() {

    // Viewbinding
    private lateinit var binding: ActivitySignUpBinding

    // 데이터 저장
    lateinit var nickName: String
    lateinit var email: String
    lateinit var id: String
    lateinit var verifyNumber: String
    lateinit var password: String
    lateinit var checkPassword: String
    lateinit var serverVerifyNumber: String

    // 닉네임, id, password, email, 개인정보제공 작성 및 동의 여부 확인
    private var nickNameFlag = false
    private var idFlag = false
    private var passwordFlag = false
    private var emailFlag = false
    private var checkAgreeFlag= false

    // 타이머 변수
    private var min = 3
    private var sec = 60

    // ApiClient의 instance 불러오기
    private val retrofit: Retrofit = ApiClient.getInstance()
    // Retrofit의 interface 구현
    private val api: LoginInterface = retrofit.create(LoginInterface::class.java)


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

        // 닉네임 정규식 확인 - nickNameListener 호출
        binding.inputNickname.addTextChangedListener(nickNameListener)

        // 아이디 정규식 확인 - idListener 호출
        binding.inputId.addTextChangedListener(idListener)

        // 비밀번호 정규식 확인 - passwordListener 호출
        binding.inputPassword.addTextChangedListener(passwordListener)

        //  비밀번호 일치  확인 -  passwordMatchListener 호출
        binding.inputCheckPassword.addTextChangedListener(passwordMatchListener)

        setOnClickListener()
    }

    private fun setOnClickListener() {

        // 인증 요청 버튼 클릭 시
        binding.btnRequestVerify.setOnClickListener {

            // 이메일 중복 검사 api 호출
            email = binding.inputEmail.text.toString()
            postCheckIsEmail(email)

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
            postCheckNewId(id)

            // 모든 항목을 올바르게 작성한 경우 - 홈 화면으로 이동, 서버에 회원 정보 update
            if(flagCheck()) {

                nickName = binding.inputNickname.text.toString()
                email = binding.inputEmail.text.toString()
                id = binding.inputId.text.toString()
                password = binding.inputPassword.text.toString()

                // 회원가입 api 호출
                postSignUp(id, password, email, nickName)

            } else {

                Toast.makeText(this, "모든 항목을 작성해주세요.", Toast.LENGTH_SHORT).show()

            }
        }

        // 로그인 버튼 클릭 시
        binding.btnLogin.setOnClickListener {
            // 로그인 화면으로 이동
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    // 닉네임 정규식
    // 대소문자, 숫자, 한글, 1~10자
    fun nickNameRegex(nickname: String): Boolean {
        return nickname.matches("[a-zA-Z[0-9][ㄱ-ㅎ|ㅏ-ㅣ|가-힣]]{1,10}".toRegex())
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

    // 3분 타이머
    private fun startTimer() {
        min--
        sec--
        // 1000 = 1초
        timer(period = 1000,) {
            runOnUiThread {
                if(sec<10) {
                    binding.textVerifyTime.text = "0$min:0$sec"
                } else {
                    binding.textVerifyTime.text = "0$min:$sec"
                }
                // 3분 후에, 타이머 중지
                if(sec==0 && min==0) {
                    cancel()
                }
                else if(sec == 0) {
                    min--
                    sec = 59
                }
                sec--
            }
        }
    }

    // id 정규식
    fun idRegex(id: String): Boolean {
        // 소문자, 숫자, _, 1~14글자 만 가능
        return id.matches("[a-z[0-9][_]]{1,14}".toRegex())
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
                    }
                }
            }
        }
    }


// password 정규식
    fun passwordRegex(password: String): Boolean {
        // 대소문자, 숫자, 특수문자, 8~16자  $@!%*#?&.
        return password.matches("[a-zA-Z[0-9][$@!%*#?&.]]{8,16}".toRegex())
    }

    val passwordListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(s: Editable?) {
            if (s != null) {
                when {
                    // 비밀번호 미입력시
                    s.isEmpty() -> {
                        Toast.makeText(this@SignUpActivity, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                        binding.btnErrorPassword.visibility = View.VISIBLE
                        binding.btnViewHide.visibility = View.GONE
                    }
                    // 비밀번호 정규식에 맞지 않는 경우
                    !passwordRegex(s.toString()) -> {
                        Log.d("msg", passwordRegex(s.toString()).toString())
                        Toast.makeText(this@SignUpActivity, "비밀번호는 8~16자 이내의 대소문자, 숫자, '!, @, #, $, &, *, .' 조합으로 입력해주세요.", Toast.LENGTH_SHORT).show()
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

    val passwordMatchListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(s: Editable?) {
            // 비밀번호가 일치하지 않을 때
            if(binding.inputCheckPassword.text != binding.inputPassword.text) {
                Toast.makeText(this@SignUpActivity, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                binding.btnErrorCheckPassword.visibility = View.VISIBLE
            } else {
                binding.btnErrorCheckPassword.visibility = View.GONE
                    passwordFlag = true
            }
        }
    }

    fun flagCheck(): Boolean {
//        checkAgreeFlag = binding.checkAgree.isChecked
//        return nickNameFlag && emailFlag && idFlag && passwordFlag && checkAgreeFlag
        // test 용
        return true
    }

    // 닉네임 중복 확인 api

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
        api.postSendVerifyEmail(data).enqueue(object:Callback<code>{

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
            }
        })
    }
}