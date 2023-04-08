package com.dwgu.linkive.MyPage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.FragmentQuitBinding

class QuitFragment : Fragment() {

    private lateinit var binding: FragmentQuitBinding

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
}