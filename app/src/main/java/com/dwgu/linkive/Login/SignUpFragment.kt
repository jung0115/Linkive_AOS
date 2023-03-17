package com.dwgu.linkive.Login

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.FragmentSignUpBinding
import java.util.regex.Pattern

class SignUpFragment : Fragment() {

    // binding
    private lateinit var binding: FragmentSignUpBinding
    // NickName filter - 대소문자, 한글만 허용
    var nickNameFilter = InputFilter {source, start, end, dest, dstart, dend ->
        val ps = Pattern.compile("^[0-9a-zA-Zㄱ-ㅣ가-힣]")
        if(!ps.matcher(source).matches()){
            ""
        } else source
    }
//    var nickNameFilter = "^[0-9a-zA-Zㄱ-ㅣ가-힣]"
//    val filter = Pattern.compile(nickNameFilter)

    // 데이터 저장
    lateinit var nickName: String
    lateinit var email: String
    lateinit var id: String
    lateinit var password: String
    lateinit var checkPassword: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnErrorPassword.visibility = View.GONE
        binding.btnErrorCheckPassword.visibility = View.GONE

        filtering()
        setOnClickListener()
    }

    private fun filtering() {


        // 닉네임 정규식 처리
//        if (filter.matcher(binding.inputNickname.text).find().toString() == "False") {
//            Toast.makeText(context, "한글, 대소문자, 숫자만 가능합니다.", Toast.LENGTH_SHORT).show()
//        }
//        if(!Pattern.matches("^[0-9a-zA-Zㄱ-ㅣ가-힣]", binding.inputNickname.text)) {
//            Toast.makeText(context, "닉네임은 10자 이내의 한글, 영문자, 숫자 조합으로 입력해주세요.", Toast.LENGTH_LONG).show()
//        }
//        if(!Pattern.matches("^0-9a-z_", binding.inputId.text)) {
//            Toast.makeText(context, "아이디는 14자 이내의 영소문자, 숫자, ‘_’ 조합으로 입력해주세요.", Toast.LENGTH_LONG).show()
//            Log.d("msg", "nicknaew woekr")
//        }
//        binding.inputNickname.addTextChangedListener {
//            object : TextWatcher {
//                override fun beforeTextChanged(s: CharSequence?,start: Int,count: Int, after: Int) {}
//
//                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//
//                override fun afterTextChanged(s: Editable?) {
//                    if (s != null) {
//                        when {
//                            !nickNameRegax(s.toString()) -> {
//                                binding.inputNickname.error = "닉네임은 10자 이내의 한글, 영문자, 숫자 조합으로 입력해주세요."
//                                Toast.makeText(context, "닉네임은 10자 이내의 한글, 영문자, 숫자 조합으로 입력해주세요.", Toast.LENGTH_LONG).show()
//                            }
//                            else -> {
//                                binding.inputNickname.error = null
//                            }
//                        }
//                    }
//                }
//            }
//        }
    }

    private fun nickNameRegax(nickName: String): Boolean {
        return nickName.matches("^[0-9a-zA-Zㄱ-ㅣ가-힣]$".toRegex())
    }

    private fun setOnClickListener() {
        // 인증 요청 버튼 클릭 시
        binding.btnRequestVerify.setOnClickListener {
            // 서버에 이메일 인증 요청 보내기, 이메일이 유효한 경우
            Toast.makeText(context, "입력한 이메일로 인증 메일이 전송되었습니다.", Toast.LENGTH_SHORT).show()
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
}