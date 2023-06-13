package com.dwgu.linkive.Search.SearchResultTab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.dwgu.linkive.Home.HomeLinkListRecycler.LinkListItem
import com.dwgu.linkive.LinkMemoApi.CreateLinkMemo.getSourceForLink
import com.dwgu.linkive.LinkMemoApi.CreateLinkMemo.getThumbnailUrl
import com.dwgu.linkive.LinkMemoApi.CreateLinkMemo.setlinkItemForms
import com.dwgu.linkive.R
import com.dwgu.linkive.Search.SearchResultRecycler.SearchResultAdapter
import com.dwgu.linkive.SearchApi.SearchAllData
import com.dwgu.linkive.SearchApi.SearchRequest
import com.dwgu.linkive.SearchApi.apiSearchAll
import com.dwgu.linkive.databinding.FragmentSearchResultAllBinding

// 검색 결과 - 전체
class SearchResultAllFragment : Fragment() {

    // ViewBinding Setting
    private var _binding: FragmentSearchResultAllBinding? = null
    private val binding get() = _binding!!

    // 검색어
    private var searchWord: String? = null

    // 검색 결과 리스트 recyclerview adapter
    private var searchResultAllItems: MutableList<LinkListItem>? = null
    private lateinit var searchResultAllAdapter: SearchResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchResultAllBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 검색어 받아오기
        searchWord = arguments?.getString("searchWord")

        // recyclerview 세팅
        initRecycler()

        // api에서 검색 결과 데이터 받아오기
        apiSearchAll(
            SearchRequest(searchWord!!, null),
            setSearchResult = {
                setSearchResult(it)
            }
        )
    }

    // recyclerview 세팅
    private fun initRecycler() {
        searchResultAllItems = mutableListOf<LinkListItem>()
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
        searchResultAllAdapter.items = searchResultAllItems!!
    }

    // 검색 결과 리스트 아이템 추가
    private fun addSearchResultAllItem(searchResultAllItem: LinkListItem) {
        searchResultAllItems!!.apply {
            add(searchResultAllItem)
        }
        searchResultAllAdapter.notifyDataSetChanged()
    }

    // 검색 결과 세팅
    private fun setSearchResult(searchResult: SearchAllData?) {
        if(searchResult != null) {
            if(searchResult.title != null) {
                for (result in searchResult.title) {
                    addSearchResultAllItem(
                        LinkListItem(
                            memoNum = result.memo_num,
                            linkTitle = result.title,
                            folderName = result.folder_name,
                            thumbnailImage = getThumbnailUrl(result.content!!.arr),
                            linkItemSource = getSourceForLink(result.link),
                            linkItemForms = setlinkItemForms(result.content.arr),
                            created_date = result.date_created
                        )
                    )
                }
            }
            if(searchResult.folder != null) {
                for (result in searchResult.folder) {
                    addSearchResultAllItem(
                        LinkListItem(
                            memoNum = result.memo_num,
                            linkTitle = result.title,
                            folderName = result.folder_name,
                            thumbnailImage = getThumbnailUrl(result.content!!.arr),
                            linkItemSource = getSourceForLink(result.link),
                            linkItemForms = setlinkItemForms(result.content.arr),
                            created_date = result.date_created
                        )
                    )
                }
            }
            if(searchResult.content != null) {
                for (result in searchResult.content) {
                    addSearchResultAllItem(
                        LinkListItem(
                            memoNum = result.memo_num,
                            linkTitle = result.title,
                            folderName = result.folder_name,
                            thumbnailImage = getThumbnailUrl(result.content!!.arr),
                            linkItemSource = getSourceForLink(result.link),
                            linkItemForms = setlinkItemForms(result.content.arr),
                            created_date = result.date_created
                        )
                    )
                }
            }
        }
    }

    // 링크 세부 페이지 열기
    private fun openLinkViewPage() {
        view?.findNavController()?.navigate(R.id.action_menu_search_to_linkViewFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}