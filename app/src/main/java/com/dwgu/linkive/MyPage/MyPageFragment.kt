package com.dwgu.linkive.MyPage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.FragmentMyPageBinding

class  MyPageFragment : Fragment() {

    // view binding
    lateinit var binding: FragmentMyPageBinding

    // recyclerview에 adapter와 data 연결
    private lateinit var basicPageSheetAdapter: BasicPageSheetAdapter
    private lateinit var customPageSheetAdapter: CustomPageSheetAdapter
    val basicPageSheetList = mutableListOf<PageSheetItem>()
    val customPageSheetList = mutableListOf<PageSheetItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyPageBinding.inflate(layoutInflater)

        basicPageSheetList.apply {
            add(PageSheetItem(1, "여행"))
            add(PageSheetItem(2, "공부"))
            add(PageSheetItem(3, "개발"))
            add(PageSheetItem(4, "일기"))
            add(PageSheetItem(5, "체크\n리스트"))
        }

        customPageSheetList.apply {
            add(PageSheetItem(1, "대구"))
            add(PageSheetItem(2, "부산"))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        basicPageSheetAdapter = BasicPageSheetAdapter(
            this.basicPageSheetList,
            sheetClick = {
                Log.d("msg", "기본 페이지 확인으로 이동")
                view?.findNavController()?.navigate(R.id.action_menu_mypage_to_pageSheetActivity)
            }
        )

        customPageSheetAdapter = CustomPageSheetAdapter(
            this.customPageSheetList,
            sheetClick = {
                Log.d("msg", "커스텀 페이지 확인으로 이동")
                view?.findNavController()?.navigate(R.id.action_menu_mypage_to_pageSheetActivity)
            }
        )

        binding.basicBtnRecycler.adapter = basicPageSheetAdapter
        binding.customBtnRecycler.adapter = customPageSheetAdapter

        setOnClickListener()
    }

    private fun setOnClickListener() {
        // 프로필 수정
        binding.btnEditProfile.setOnClickListener{
            Log.d("msg", "회원 정보 수정 화면으로 이동")
            view?.findNavController()?.navigate(R.id.action_menu_mypage_to_editProfileFragment)
        }

        // 문의하기
        binding.btnInquiry.setOnClickListener {
            Log.d("msg", "문의하기 화면으로 이동")
            view?.findNavController()?.navigate(R.id.action_menu_mypage_to_inquiryFragment)
        }

        // 로그아웃
        binding.btnLogout.setOnClickListener {
            // 차후 수정 필요


            // 로그아웃 후, 로그인 화면으로 이동
            view?.findNavController()?.navigate(R.id.action_menu_mypage_to_loginActivity)
        }

        // 회원탈퇴
        binding.btnQuit.setOnClickListener {
            Log.d("msg", "회원 탈퇴 화면으로 이동")
            view?.findNavController()?.navigate(R.id.action_menu_mypage_to_quitFragment)
        }
    }
}