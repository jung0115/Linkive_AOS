package com.dwgu.linkive.Search.SearchResultTab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.dwgu.linkive.Home.HomeLinkListRecycler.LinkListItem
import com.dwgu.linkive.LinkView.LinkViewFragment
import com.dwgu.linkive.R
import com.dwgu.linkive.Search.SearchResultRecycler.SearchResultAdapter
import com.dwgu.linkive.databinding.FragmentSearchResultContentsBinding

// 검색 결과 - 본문
class SearchResultContentsFragment : Fragment() {

    // ViewBinding Setting
    lateinit var binding: FragmentSearchResultContentsBinding

    // 검색어
    private var searchWord: String? = null

    // 검색 결과 리스트 recyclerview adapter
    private var searchResultContentsItems = mutableListOf<LinkListItem>()
    private lateinit var searchResultContentsAdapter: SearchResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchResultContentsBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 검색어 받아오기
        searchWord = arguments?.getString("searchWord")

        // recyclerview 세팅
        initRecycler()

        // 테스트 데이터
        addSearchResultContentsItem(LinkListItem("제목입니다", "검색", null, "twitter", mutableListOf("link", "place")))
        addSearchResultContentsItem(LinkListItem("테스트 검색제목", "검색", null, null, null))
    }

    // recyclerview 세팅
    private fun initRecycler() {
        // 링크 리스트 recyclerview 세팅
        searchResultContentsAdapter = SearchResultAdapter(
            requireContext(),
            searchWord!!,
            "contents",
            onClickLinkItem = {
                openLinkViewPage()
            }
        )
        binding.recyclerviewSearchResultContents.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerviewSearchResultContents.adapter = searchResultContentsAdapter
        binding.recyclerviewSearchResultContents.isNestedScrollingEnabled = false // 스크롤을 매끄럽게 해줌
        searchResultContentsAdapter.items = searchResultContentsItems
    }

    // 검색 결과 리스트 아이템 추가
    private fun addSearchResultContentsItem(searchResultContentsItem: LinkListItem) {
        searchResultContentsItems.apply {
            add(searchResultContentsItem)
        }
        searchResultContentsAdapter.notifyDataSetChanged()
    }

    // 링크 세부 페이지 열기
    private fun openLinkViewPage() {
        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .add(R.id.nav_host_fragment, LinkViewFragment())
            .commit()
    }
}