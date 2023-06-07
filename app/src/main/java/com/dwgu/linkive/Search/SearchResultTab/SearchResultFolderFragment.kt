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
import com.dwgu.linkive.LinkMemoApi.ViewLinkMemo.ViewLinkMemo
import com.dwgu.linkive.LinkView.LinkViewFragment
import com.dwgu.linkive.R
import com.dwgu.linkive.Search.SearchResultRecycler.SearchResultAdapter
import com.dwgu.linkive.SearchApi.SearchRequest
import com.dwgu.linkive.SearchApi.apiSearch
import com.dwgu.linkive.databinding.FragmentSearchResultFolderBinding

// 검색 결과 - 폴더명
class SearchResultFolderFragment : Fragment() {

    // ViewBinding Setting
    private var _binding: FragmentSearchResultFolderBinding? = null
    private val binding get() = _binding!!

    // 검색어
    private var searchWord: String? = null

    // 검색 결과 리스트 recyclerview adapter
    private var searchResultFolderItems: MutableList<LinkListItem>? = null
    private lateinit var searchResultFolderAdapter: SearchResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchResultFolderBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 검색어 받아오기
        searchWord = arguments?.getString("searchWord")

        // recyclerview 세팅
        initRecycler()

        // api에서 검색 결과 데이터 받아오기
        apiSearch(
            SearchRequest(searchWord!!, "folder"),
            setSearchResult = {
                setSearchResult(it)
            }
        )
    }

    // recyclerview 세팅
    private fun initRecycler() {
        searchResultFolderItems = mutableListOf<LinkListItem>()
        // 링크 리스트 recyclerview 세팅
        searchResultFolderAdapter = SearchResultAdapter(
            requireContext(),
            searchWord!!,
            "folder",
            onClickLinkItem = {
                openLinkViewPage()
            }
        )
        binding.recyclerviewSearchResultFolder.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerviewSearchResultFolder.adapter = searchResultFolderAdapter
        binding.recyclerviewSearchResultFolder.isNestedScrollingEnabled = false // 스크롤을 매끄럽게 해줌
        searchResultFolderAdapter.items = searchResultFolderItems!!
    }

    // 검색 결과 리스트 아이템 추가
    private fun addSearchResultFolderItem(searchResultFolderItem: LinkListItem) {
        searchResultFolderItems!!.apply {
            add(searchResultFolderItem)
        }
        searchResultFolderAdapter.notifyDataSetChanged()
    }

    // 검색 결과 세팅
    private fun setSearchResult(searchResult: MutableList<ViewLinkMemo>?) {
        if(searchResult != null) {
            for (result in searchResult) {
                addSearchResultFolderItem(
                    LinkListItem(
                        memoNum = result.memo_num,
                        linkTitle = result.title,
                        folderName = result.folder_name,
                        thumbnailImage = getThumbnailUrl(result.content!!.arr),
                        linkItemSource = getSourceForLink(result.link),
                        linkItemForms = null,
                        created_date = result.date_created
                    )
                )
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