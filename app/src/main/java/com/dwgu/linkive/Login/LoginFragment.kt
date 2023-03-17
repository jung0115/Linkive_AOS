package com.dwgu.linkive.Login

import android.content.Context
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.text.InputType
import android.text.InputType.*
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.FragmentLoginBinding
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment(){

    // binding
    private lateinit var binding: FragmentLoginBinding

    // 변수들
    private var id = "11"
    private var password = "12"
    lateinit var inputId: String
    lateinit var inputPassword: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnErrorId.visibility = View.GONE
        binding.btnErrorPassword.visibility = View.GONE

        setOnClickListener()
    }

    private fun setOnClickListener() {

        // 비밀번호 보여주기, 숨기기 버튼
        binding.btnViewHide.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked) {
                // 비밀번호를 보이게
                binding.inputPassword.inputType = TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.btnViewHide.setBackgroundResource(R.drawable.view)
            } else {
                // 비밀번호가 안보이게
                binding.inputPassword.inputType = TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_PASSWORD
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
        }
        binding.btnFindPassword.setOnClickListener {
            Log.d("msg", "find password work")
        }

        // 회원 가입 버튼 클릭 시, 회원 가입 화면으로 이동
        binding.btnSignUp.setOnClickListener {
            Log.d("msg", "sign up work")
        }

        // 소셜 로그인
        binding.btnKakaoLogin.setOnClickListener {
            Log.d("msg", "kakao login work")
        }
        binding.btnNaverLogin.setOnClickListener {
            Log.d("msg", "naver login work")
        }
        binding.btnGoogleLogin.setOnClickListener {
            Log.d("msg", "google login work")
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}