package com.dwgu.linkive.Home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.dwgu.linkive.Home.HomeLinkListRecycler.LinkListAdapter
import com.dwgu.linkive.Home.HomeLinkListRecycler.LinkListItem
import com.dwgu.linkive.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    // ViewBinding Setting
    lateinit var binding: FragmentHomeBinding

    // 링크 리스트 recyclerview adapter
    private var linkListItems = mutableListOf<LinkListItem>()
    private lateinit var linkListAdapter: LinkListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater);

        return binding.root
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
    }

    // recyclerview 세팅
    private fun initRecycler() {
        // 링크 리스트 recyclerview 세팅
        linkListAdapter = LinkListAdapter(
            requireContext()
        )
        binding.recyclerviewLinkList.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerviewLinkList.adapter = linkListAdapter
        linkListAdapter.items = linkListItems
    }

    // 링크 리스트 아이템 추가
    private fun addLinkListItem(linkListItem: LinkListItem) {
        linkListItems.apply {
            add(linkListItem)
        }
        linkListAdapter.notifyDataSetChanged()
    }
}