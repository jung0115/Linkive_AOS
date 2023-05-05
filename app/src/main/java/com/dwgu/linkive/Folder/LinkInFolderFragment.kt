package com.dwgu.linkive.Folder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.dwgu.linkive.Folder.LinkInFolderAdapter.LinkInFolderAdapter
import com.dwgu.linkive.Home.HomeLinkListRecycler.LinkListAdapter
import com.dwgu.linkive.Home.HomeLinkListRecycler.LinkListItem
import com.dwgu.linkive.LinkView.LinkViewFragment
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.FragmentFolderBinding
import com.dwgu.linkive.databinding.FragmentHomeBinding
import com.dwgu.linkive.databinding.FragmentLinkInFolderBinding

class LinkInFolderFragment : Fragment() {

    // 링크 리스트 recyclerview adapter
    private var linkListItems = mutableListOf<LinkListItem>()
    private lateinit var linkInFolderAdapter: LinkInFolderAdapter

    private var _binding: FragmentLinkInFolderBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLinkInFolderBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // recyclerview 세팅
        initRecycler()

        // 테스트 데이터
        addLinkListItem(LinkListItem("테스트1", "폴더1",
            "https:/img.youtube.com/vi/UYGud3qJeFI/default.jpg",
            "instagram", mutableListOf("text", "image")))
        addLinkListItem(LinkListItem("테스트2", "폴더2", null, "twitter", mutableListOf("link", "place")))
        addLinkListItem(LinkListItem("테스트3", null, null, "naver_blog", mutableListOf("text", "image", "link", "place", "code", "checkbox")))
        addLinkListItem(LinkListItem("테스트4", null, null, null, null))
        addLinkListItem(LinkListItem("테스트5", "폴더5", null, null, null))
        addLinkListItem(LinkListItem("테스트6", null, null, "twitter", null))
        addLinkListItem(LinkListItem("테스트7", "폴더7", null, "twitter", null))
        addLinkListItem(LinkListItem("테스트8", null, null, "twitter", null))

        //메뉴 버튼 클릭 시
        binding.btnMenu.setOnClickListener {
            // 폴더 관리 바텀 시트
            val bottomSheetFragment = LinkInFolderMenuBottomSheetFragment()
            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
        }
    }

    private fun initRecycler() {
        // 링크 리스트 recyclerview 세팅
        linkInFolderAdapter = LinkInFolderAdapter(
            requireContext(),
            onClickLinkItem = {
                openLinkViewPage()
            }
        )
        binding.recyclerviewLinkList.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerviewLinkList.adapter = linkInFolderAdapter
        binding.recyclerviewLinkList.isNestedScrollingEnabled = false // 스크롤을 매끄럽게 해줌
        linkInFolderAdapter.items = linkListItems
    }

    // 링크 리스트 아이템 추가
    private fun addLinkListItem(linkListItem: LinkListItem) {
        linkListItems.apply {
            add(linkListItem)
        }
        linkInFolderAdapter.notifyDataSetChanged()
    }


    // 링크 세부 페이지 열기
    private fun openLinkViewPage() {
        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .add(R.id.nav_host_fragment, LinkViewFragment())
            .commit()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}