package com.dwgu.linkive.MyPage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.dwgu.linkive.Api.ApiClient
import com.dwgu.linkive.Login.loginService.LoginInterface
import com.dwgu.linkive.MyPage.MyPageRecycler.BasicPageSheetAdapter
import com.dwgu.linkive.MyPage.MyPageRecycler.CustomPageSheetAdapter
import com.dwgu.linkive.MyPage.MyPageRecycler.PageSheetItem
import com.dwgu.linkive.MyPage.myPageService.MyPageInterface
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.FragmentMyPageBinding
import retrofit2.Retrofit

class  MyPageFragment : Fragment() {

    // view binding
    lateinit var binding: FragmentMyPageBinding

    // recyclerview에 adapter와 data 연결
    private lateinit var basicPageSheetAdapter: BasicPageSheetAdapter
    private lateinit var customPageSheetAdapter: CustomPageSheetAdapter
    val basicPageSheetList = mutableListOf<PageSheetItem>()
    val customPageSheetList = mutableListOf<PageSheetItem>()

    // ApiClient의 instance 불러오기
    private val retrofit: Retrofit = ApiClient.getInstance()
    // Retrofit의 interface 구현
    private val api: MyPageInterface = retrofit.create(MyPageInterface::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyPageBinding.inflate(layoutInflater)

        // 사용자 프로필 이미지 가져오기
        api.getProfileImg()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // recyclerview 세팅
        initRecycler()

        addBasicPageSheetItem(PageSheetItem(1, "여행"))
        addBasicPageSheetItem(PageSheetItem(2, "공부"))
        addBasicPageSheetItem(PageSheetItem(3, "개발"))
        addBasicPageSheetItem(PageSheetItem(4, "일기"))
        addBasicPageSheetItem(PageSheetItem(5, "체크\n리스트"))

        addCustomPageSheetItem(PageSheetItem(1, "애니"))
        addCustomPageSheetItem(PageSheetItem(2, "맛집"))
        addCustomPageSheetItem(PageSheetItem(3, "숙소"))
        addCustomPageSheetItem(PageSheetItem(4, "+"))

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
//            view?.findNavController()?.navigate(R.id.action_menu_mypage_to_inquiryFragment)
            view?.findNavController()?.navigate(R.id.action_menu_mypage_to_pageSheetFragment)
        }

        // 로그아웃
        binding.btnLogout.setOnClickListener {
            // 차후 수정 필요


            // 로그아웃 후, 로그인 화면으로 이동
            Toast.makeText(requireContext(), "로그아웃!", Toast.LENGTH_SHORT).show()
            Log.d("msg", "logout")
            view?.findNavController()?.navigate(R.id.action_menu_mypage_to_loginActivity)
        }

        // 회원탈퇴
        binding.btnQuit.setOnClickListener {
            Log.d("msg", "회원 탈퇴 화면으로 이동")
            view?.findNavController()?.navigate(R.id.action_menu_mypage_to_quitFragment)
        }
    }

    // 리사이클러 초기화
    private fun initRecycler() {
        basicPageSheetAdapter = BasicPageSheetAdapter(
            requireContext(),
            sheetClick = {
                moveToPageSheet()
            }
        )

        binding.basicBtnRecycler.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.basicBtnRecycler.adapter = basicPageSheetAdapter
        binding.basicBtnRecycler.isNestedScrollingEnabled = false
        basicPageSheetAdapter.data = basicPageSheetList

        customPageSheetAdapter = CustomPageSheetAdapter(
            requireContext(),
            sheetClick = {
                moveToPageSheet()
            }
        )

        binding.customBtnRecycler.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.customBtnRecycler.adapter = customPageSheetAdapter
        binding.customBtnRecycler.isNestedScrollingEnabled = false
        customPageSheetAdapter.data = customPageSheetList
    }

    private fun addBasicPageSheetItem(item: PageSheetItem) {
        basicPageSheetList.apply {
            add(item)
        }
        basicPageSheetAdapter.notifyDataSetChanged()
    }

    private fun addCustomPageSheetItem(item: PageSheetItem) {
        customPageSheetList.apply {
            add(item)
        }
        customPageSheetAdapter.notifyDataSetChanged()
    }

    private fun moveToPageSheet() {
        Log.d("msg", "moveToPageSheet")
        view?.findNavController()?.navigate(R.id.action_menu_mypage_to_showPageSheetFragment)
//        requireActivity()
//            .supportFragmentManager
//            .beginTransaction()
//            .add(R.id.nav_host_fragment, ShowPageSheetFragment())
//            .commit()
    }
}