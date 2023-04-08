package com.dwgu.linkive.MyPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListener()
    }

    private fun setOnClickListener() {
        // 취소버튼
        binding.btnCancelEditProfile.setOnClickListener {
            Toast.makeText(activity, "프로필 수정이 취소되었습니다.", Toast.LENGTH_SHORT).show()

            // 마이페이지로 이동
            view?.findNavController()?.navigate(R.id.action_editProfileFragment_to_menu_mypage)
        }

        // 확인버튼
        binding.btnCheckEditProfile.setOnClickListener {
            Toast.makeText(activity, "프로필 수정이 저장되었습니다.", Toast.LENGTH_SHORT).show()
            // 서버에 데이터 저장

            // 마이페이지로 이동
            view?.findNavController()?.navigate(R.id.action_editProfileFragment_to_menu_mypage)
        }
    }
}