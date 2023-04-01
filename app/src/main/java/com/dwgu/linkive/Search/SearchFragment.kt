package com.dwgu.linkive.Search

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.dwgu.linkive.Search.SearchResultTab.*
import com.dwgu.linkive.databinding.FragmentSearchBinding
import com.google.android.material.tabs.TabLayout

// 검색 페이지
class SearchFragment : Fragment() {

    // ViewBinding Setting
    lateinit var binding: FragmentSearchBinding

    // viewPager
    lateinit var viewPagers: ViewPager
    lateinit var tabLayouts: TabLayout

    // 검색어
    private var searchWord: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 검색어 입력 확인
        binding.edittextSearchInput.setOnKeyListener { _, keyCode, event ->
            // 엔터가 눌렸을 때
            if((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // 검색어 없을 때 보여주던 아이콘 이미지 숨기기
                binding.notInput.visibility = View.GONE

                // 검색어
                searchWord = binding.edittextSearchInput.text.toString()

                // 검색 결과 보여주는 Tab 보여주기
                binding.taylayoutSearchResult.visibility = View.VISIBLE
                binding.viewpagerSearchResult.visibility = View.VISIBLE

                // viewPager 세팅
                initViewPager()

                true
            }
            else {
                false
            }
        }
    }

    // viewPager 세팅
    private fun initViewPager() {
        viewPagers = binding.viewpagerSearchResult
        tabLayouts = binding.taylayoutSearchResult

        // 검색 결과 탭에 보여줄 Fragment
        val searchResultAllFragment = SearchResultAllFragment()
        val searchResultTitleFragment = SearchResultTitleFragment()
        val searchResultContentsFragment = SearchResultContentsFragment()
        val searchResultFolderFragment = SearchResultFolderFragment()
        val searchResultPlaceFragment = SearchResultPlaceFragment()

        // 검색 결과 tab에 검색어 전달
        val bundle = Bundle()
        bundle.putString("searchWord", searchWord)
        searchResultAllFragment.arguments = bundle
        searchResultTitleFragment.arguments = bundle
        searchResultContentsFragment.arguments = bundle
        searchResultFolderFragment.arguments = bundle
        searchResultPlaceFragment.arguments = bundle

        // 검색 결과 탭에 각 Fragment 배치
        val adapter = SearchViewPagerAdapter(requireActivity().supportFragmentManager)
        adapter.addFragment(searchResultAllFragment, "전체")
        adapter.addFragment(searchResultTitleFragment, "제목")
        adapter.addFragment(searchResultContentsFragment, "본문")
        adapter.addFragment(searchResultFolderFragment, "폴더명")
        adapter.addFragment(searchResultPlaceFragment, "장소")

        viewPagers.adapter = adapter
        tabLayouts.setupWithViewPager(viewPagers)

        // Tab 전환
        tabLayouts.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
            }
        })
    }
}