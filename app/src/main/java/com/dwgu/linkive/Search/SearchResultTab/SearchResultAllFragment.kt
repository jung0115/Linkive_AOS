package com.dwgu.linkive.Search.SearchResultTab

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.dwgu.linkive.Home.HomeLinkListRecycler.LinkListAdapter
import com.dwgu.linkive.Home.HomeLinkListRecycler.LinkListItem
import com.dwgu.linkive.LinkView.LinkVIewFragment
import com.dwgu.linkive.R
import com.dwgu.linkive.Search.SearchResultRecycler.SearchResultAdapter
import com.dwgu.linkive.databinding.FragmentSearchResultAllBinding

// 검색 결과 - 전체
class SearchResultAllFragment : Fragment() {

    // ViewBinding Setting
    lateinit var binding: FragmentSearchResultAllBinding

    // 검색어
    private var searchWord: String? = null

    // 검색 결과 리스트 recyclerview adapter
    private var searchResultAllItems = mutableListOf<LinkListItem>()
    private lateinit var searchResultAllAdapter: SearchResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchResultAllBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 검색어 받아오기
        searchWord = arguments?.getString("searchWord")

        // recyclerview 세팅
        initRecycler()

        // 테스트 데이터
        addSearchResultAllItem(LinkListItem("검색 테스트", "폴더1",
            "https:/img.youtube.com/vi/UYGud3qJeFI/default.jpg",
            "instagram", mutableListOf("text", "image")))
        addSearchResultAllItem(LinkListItem("제목입니다", "검색", null, "twitter", mutableListOf("link", "place")))
        addSearchResultAllItem(LinkListItem("제목입니다2", "폴더검색", null, "naver_blog", mutableListOf("link", "place")))
        addSearchResultAllItem(LinkListItem("테스트 검색제목", "검색", null, null, null))
    }

    // recyclerview 세팅
    private fun initRecycler() {
        // 링크 리스트 recyclerview 세팅
        searchResultAllAdapter = SearchResultAdapter(
            requireContext(),
            searchWord!!,
            "all",
            onClickLinkItem = {
                openLinkViewPage()
            }
        )
        binding.recyclerviewSearchResultAll.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerviewSearchResultAll.adapter = searchResultAllAdapter
        binding.recyclerviewSearchResultAll.isNestedScrollingEnabled = false // 스크롤을 매끄럽게 해줌
        searchResultAllAdapter.items = searchResultAllItems
    }

    // 검색 결과 리스트 아이템 추가
    private fun addSearchResultAllItem(searchResultAllItem: LinkListItem) {
        searchResultAllItems.apply {
            add(searchResultAllItem)
        }
        searchResultAllAdapter.notifyDataSetChanged()
    }

    // 링크 세부 페이지 열기
    private fun openLinkViewPage() {
        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .add(R.id.nav_host_fragment, LinkVIewFragment())
            .commit()
    }
}