package com.dwgu.linkive.Home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.dwgu.linkive.Home.CreateLinkToUrl.CreateLinkToUrlDialog
import com.dwgu.linkive.Home.HomeLinkListRecycler.LinkListAdapter
import com.dwgu.linkive.Home.HomeLinkListRecycler.LinkListItem
import com.dwgu.linkive.LinkMemoApi.CreateLinkMemo.apiGetAllFolders
import com.dwgu.linkive.LinkMemoApi.CreateLinkMemo.apiViewLinkMemo
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    // ViewBinding Setting
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // 링크 리스트 recyclerview adapter
    private var linkListItems: MutableList<LinkListItem>? = null
    private lateinit var linkListAdapter: LinkListAdapter

    // 링크 리스트 정렬 Spinner
    // 정렬 기준 - 최신순, 제목순
    private val linkListSortList = mutableListOf<String>()
    // Spinner 어댑터
    private var linkListSortAdapter: ArrayAdapter<String>? = null
    // 선택된 정렬 기준
    private var selectLinkListSort: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // recyclerview 세팅
        initRecycler()

        // 테스트 데이터
        /*addLinkListItem(LinkListItem("테스트1", "폴더1",
            "https:/img.youtube.com/vi/UYGud3qJeFI/default.jpg",
            "instagram", mutableListOf("text", "image"), ""))*/

        // 주소 검색 테스트
        //apiGetKakaoAddress("카카오 부산")

        // 링크 전체 조회 api -> 조회 후 데이터 추가
        //testLogin()
        apiViewLinkMemo(
            addLinkList = {
                addLinkListItem(it)
            }
        )

        // 링크 리스트 정렬 Spinner
        linkListSortList.add(getString(R.string.spinner_sort_new)) // 최신순
        linkListSortList.add(getString(R.string.spinner_sort_title))   // 제목순
        linkListSortAdapter = ArrayAdapter(requireContext(), R.layout.item_spinner_link_list_sort, linkListSortList)
        binding.spinnerLinkListSort.adapter = linkListSortAdapter

        // 정렬 Spinner 누르면 정렬 기준 선택 리스트 나타남
        binding.spinnerLinkListSort.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // 선택된 정렬 기준
                selectLinkListSort = binding.spinnerLinkListSort.getSelectedItem().toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        // Floating 버튼 선택 시 URL로 링크 추가 Dialog 열기
        binding.btnCreateLinkToUrl.setOnClickListener {
            val dialog = CreateLinkToUrlDialog(
                requireContext()
            )
            dialog.setRefreshHomeListener(object: CreateLinkToUrlDialog.RefreshHomeListener{
                override fun refreshHomeListener() {
                    // 링크 리스트 아이템 Refresh
                    initRecycler()
                    apiViewLinkMemo(
                        addLinkList = {
                            addLinkListItem(it)
                        }
                    )
                }
            })
            dialog.show(requireActivity().supportFragmentManager, "CreateLinkToUrlDialog")
        }
    }

    // recyclerview 세팅
    private fun initRecycler() {
        linkListItems = mutableListOf<LinkListItem>()

        // 링크 리스트 recyclerview 세팅
        linkListAdapter = LinkListAdapter(
            requireContext(),
            onClickLinkItem = {
                openLinkViewPage()
            }
        )
        binding.recyclerviewLinkList.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerviewLinkList.adapter = linkListAdapter
        binding.recyclerviewLinkList.isNestedScrollingEnabled = false // 스크롤을 매끄럽게 해줌
        linkListAdapter.items = linkListItems!!
    }

    // 링크 리스트 아이템 추가
    private fun addLinkListItem(linkListItem: LinkListItem) {
        linkListItems!!.apply {
            add(linkListItem)
        }
        linkListAdapter.notifyDataSetChanged()
    }

    // 링크 세부 페이지 열기
    private fun openLinkViewPage() {
        view?.findNavController()?.navigate(R.id.action_menu_home_to_linkViewFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}