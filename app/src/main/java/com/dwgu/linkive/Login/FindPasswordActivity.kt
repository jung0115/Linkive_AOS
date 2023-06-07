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
import com.dwgu.linkive.Api.ApiClient
import com.dwgu.linkive.Login.loginService.LoginInterface
import com.dwgu.linkive.Login.loginService.code
import com.dwgu.linkive.Login.loginService.email
import com.dwgu.linkive.Login.loginService.findPw
import com.dwgu.linkive.Login.loginService.newId
import com.dwgu.linkive.Login.loginService.newPw
import com.dwgu.linkive.Login.loginService.result
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.ActivityFindPasswordBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import kotlin.concurrent.timer

class FindPasswordActivity : AppCompatActivity() {

    // viewBinding 선언
    lateinit var binding: ActivityFindPasswordBinding

    // 타이머 변수
    private var min = 3
    private var sec = 60

    // 데이터 저장
    lateinit var email: String
    lateinit var id: String
    lateinit var verifyNum: String
    lateinit var serverVerifyNum: String
    lateinit var pw: String
    lateinit var repw: String

    // flag
    private var idFlag = false
    private var emailFlag = false
    private var passwordFlag = false

    // ApiClient의 instance 불러오기
    private val retrofit: Retrofit = ApiClient.getInstance()
    // Retrofit의 interface 구현
    private val api: LoginInterface = retrofit.create(LoginInterface::class.java)


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

        // 아이디 확인 버튼
        binding.btnIdVerify.setOnClickListener {

            // 아이디 중복 확인 api
            id = binding.inputId.text.toString()
            postCheckNewId(id)

        }

        // 인증 요청 버튼
        binding.btnRequestVerify.setOnClickListener {

            // 이메일 중복 확인 api
            email = binding.inputEmail.text.toString()
            postCheckIsEmail(email)
        }

        // 인증하기 버튼
        binding.btnVerify.setOnClickListener {

            if(emailFlag) {
                if(verifyNum == serverVerifyNum) {
                    // 이메일 인증 성공
                    Log.d("find pw", "email verify match")
                    Toast.makeText(this@FindPasswordActivity, "인증이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    binding.btnErrorEmail.visibility = View.GONE
                } else {
                    // 인증번호를 다시 확인해주세요.
                    Toast.makeText(this@FindPasswordActivity, "인증번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@FindPasswordActivity, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }

        }

        // 비밀번호 정규식 확인 - passwordListener 호출
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

        // 비밀번호 일치 확인 - passwordMatchListener 호출
        binding.inputCheckPassword.addTextChangedListener(passwordMatchListener)

        // 취소 버튼
        binding.btnCheckFindPassword.setOnClickListener {
            // 로그인 화면으로 이동
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // 확인 버튼
        binding.btnCheckFindPassword.setOnClickListener {
            // 모든 항목을 올바르게 작성한 경우
            if(idFlag  &&  passwordFlag && emailFlag) {
                // 비밀번호 변경 api
                postFindPassword(id, pw)
            }
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
            Log.d("find pw - pw listener work", s.toString())
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
                        Log.d("find pw - error", passwordRegex(s.toString()).toString())
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

    // 아이디 중복 확인 api
    private fun postCheckNewId(id: String) {
        var data = newId(id)
        api.postCheckNewId(data).enqueue(object :Callback<result>{
            override fun onFailure(call: Call<result>, t: Throwable) {}

            override fun onResponse(call: Call<result>, response: Response<result>) {
                var code = response.code()
                Log.d("find pw - code", code.toString())
                // 서버에 존재하지 않는 아이디
                if(code == 200) {
                    Log.d("find pw", "id not exists")
                }
                // 서버에 존재하는 아이디
                else if(code == 409) {
                    Log.d("find pw", "id exists")
                    idFlag = true
                    binding.btnErrorId.visibility = View.GONE
                }
                else {
                    Log.d("find  pw", "other error")
                    Toast.makeText(this@FindPasswordActivity, "일시적인 오류입니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    // 이메일 중복 확인 api
    private fun postCheckIsEmail(mail: String){
        var data = email(mail)
        api.postCheckIsEmail(data).enqueue(object :Callback<result>{
            override fun onFailure(call: Call<result>, t: Throwable) {}

            override fun onResponse(call: Call<result>, response: Response<result>) {

                var code = response.code()

                // 서버에 존재하는 이메일
                if(code == 200) {
                    Toast.makeText(this@FindPasswordActivity, "인증번호가 발송되었습니다..", Toast.LENGTH_SHORT).show()
                    binding.btnErrorEmail.visibility = View.GONE

                    // 올바른 이메일 표시
                    emailFlag = true

                    // 3분 타이머 시작
                    startTimer()

                    //  이메일 인증 api 호출
                    verifyNum = binding.inputVerifyNumber.text.toString()
                    postSendVerifyEmailFindPassword(mail, id)
                }
                // 서버에 존재하지 않는 이메일
                else if(code == 400) {
                    Toast.makeText(this@FindPasswordActivity, "존재하지 않는 이메일입니다.", Toast.LENGTH_SHORT).show()
                    binding.btnErrorEmail.visibility = View.VISIBLE
                }

            }
        })
    }

    //  인증번호 보내기 api
    private fun postSendVerifyEmailFindPassword(mail: String, id: String){
        var data = findPw(mail, id)
        //  인증번호 전송 실패
        api.postSendVerifyEmailFindPassword(data).enqueue(object:Callback<code>{
            override fun onFailure(call: Call<code>, t: Throwable) {

                Log.d("find pw", "unvaild email, id")
                Log.d("find pw", t.toString())

            }

            override fun onResponse(call: Call<code>, response: Response<code>) {

                Log.d("find pw", "vaild email, id")
                serverVerifyNum = response.code().toString()
                Log.d("find pw", serverVerifyNum)

                verifyNum = binding.inputVerifyNumber.text.toString()
                Log.d("find pw", verifyNum)

            }
        })
    }

    // 비밀번호 변경 api
    private fun postFindPassword(id: String, pw: String) {
        var data = newPw(id, pw)
        api.postFindPassword(data).enqueue(object :Callback<result>{
            override fun onFailure(call: Call<result>, t: Throwable) {
                Log.d("find pw - fail", t.toString())
            }

            override fun onResponse(call: Call<result>, response: Response<result>) {
                var code = response.code()
                Log.d("find pw - success", code.toString())
                if(code == 200) {
                    Log.d("find pw", "success")
                    Toast.makeText(this@FindPasswordActivity, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show()
                } else if(code == 401) {
                    Log.d("find pw", "error")
                    Toast.makeText(this@FindPasswordActivity, "해당 id의 사용자가 없습니다. 아이디를 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}