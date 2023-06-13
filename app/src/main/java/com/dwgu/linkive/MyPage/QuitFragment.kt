package com.dwgu.linkive.MyPage

import android.os.Bundle
import android.provider.Settings.Global
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.dwgu.linkive.Api.ApiClient
import com.dwgu.linkive.Login.GloabalApplication
import com.dwgu.linkive.MyPage.myPageService.MyPageInterface
import com.dwgu.linkive.MyPage.myPageService.emails
import com.dwgu.linkive.MyPage.myPageService.result
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.FragmentQuitBinding
import kotlinx.coroutines.GlobalScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class QuitFragment : Fragment() {

    private lateinit var binding: FragmentQuitBinding

    // ApiClient의 instance 불러오기
    private val retrofit: Retrofit = ApiClient.getInstance()
    // Retrofit의 interface 구현
    private val api: MyPageInterface = retrofit.create(MyPageInterface::class.java)

    // 변수들
    lateinit var email: String
    lateinit var pw: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuitBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListener()
    }

    private fun setOnClickListener() {
        // 확인
        binding.btnCheckExit.setOnClickListener {
            // 서버에 회원 정보 삭제


            // 회원 탈퇴 api
            email = binding.inputEmail.text.toString()
            pw = binding.inputPassword.text.toString()
            postEmailLoginDelete(email, pw)
            Toast.makeText(activity, "탈퇴되었습니다.", Toast.LENGTH_SHORT).show()

            // 로그인 화면으로 이동
            view?.findNavController()?.navigate(R.id.action_quitFragment_to_loginActivity)
        }

        // 취소
        binding.btnCancelExit.setOnClickListener {
            Log.d("msg", "마이페이지로 이동")
            Toast.makeText(activity, "탈퇴가 취소되었습니다.", Toast.LENGTH_SHORT).show()
            // 마이페이지로 이동
            view?.findNavController()?.navigate(R.id.action_quitFragment_to_menu_mypage)
        }

    }

    private fun postEmailLoginDelete(mail: String, password: String) {

        var accessToken = GloabalApplication.prefs.getString("accessToken", "")
        var refreshToken = GloabalApplication.prefs.getString("refreshToken", "")

        var data = emails(mail, password)
        api.postEmailLoginDelete(accessToken, refreshToken, data).enqueue(object :Callback<result>{
            override fun onFailure(call: Call<result>, t: Throwable) {
                Log.d("my page - delete user fail", t.toString())
            }

            override fun onResponse(call: Call<result>, response: Response<result>) {
                Log.d("my page ", "delete user")
                Log.d("my page", response.body()?.result.toString())
            }
        })
    }
}