package com.dwgu.linkive.LinkView

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dwgu.linkive.LinkView.LinkViewRecycler.*
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.FragmentLinkViewBinding


class LinkVIewFragment : Fragment() {

    // ViewBinding Setting
    lateinit var binding: FragmentLinkViewBinding

    // 링크 View 페이지 아이템 Recyclerview
    private val linkViewItems = mutableListOf<LinkViewItem>()
    private lateinit var linkViewAdapter: LinkViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLinkViewBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 페이지 정보 세팅 - 제목, 폴더, 출처 플랫폼
        setLinkViewInformation("제목입니다", "instagram", "폴더1")

        // recyclerview 세팅
        initRecycler()

        // 테스트 데이터
        addLinkViewItem(LinkViewImageItem("https:/img.youtube.com/vi/UYGud3qJeFI/default.jpg"))
        addLinkViewItem(LinkViewTextItem("글 테스트입니다.\n테스트 글입니다."))
        addLinkViewItem(LinkViewPlaceItem("서울 송파구 올림픽로 240", "잠실동 40-1"))
        addLinkViewItem(LinkViewLinkItem("백준 - 토마토(7569)", "https://www.acmicpc.net/problem/7569"))
        addLinkViewItem(LinkViewCodeItem("System.out.print(“Hello, World!”);\n\n" +
                "while(i < 10) {\ni++;\n}\n\nSystem.out.print(“Hello, World!”);\nSystem.out.print(“Hello, World!”);"))
        addLinkViewItem(LinkViewCheckboxItem("할 일 1", true))
        addLinkViewItem(LinkViewCheckboxItem("할 일 2", false))
        addLinkViewItem(LinkViewImageItem("https:/img.youtube.com/vi/UYGud3qJeFI/default.jpg"))
        addLinkViewItem(LinkViewTextItem("글 테스트입니다.\n테스트 글입니다."))
    }

    // 페이지 정보 세팅 - 제목, 폴더, 출처 플랫폼
    private fun setLinkViewInformation(title: String, source: String?, folder: String?) {
        // 제목
        binding.textviewLinkViewTitle.text = title

        // 출처 플랫폼
        // 출처 플랫폼이 존재하는 경우
        if(source != null) {
            val iconResourceName = "ic_link_list_item_source_" + source
            val iconResourceId = resources.getIdentifier(iconResourceName, "drawable", requireContext().packageName)
            binding.imgLinkViewSource.setImageResource(iconResourceId)
        }

        // 폴더
        // 폴더가 존재하는 경우
        if(folder != null) {
            binding.imgLinkViewFolder.setImageResource(R.drawable.ic_folder_exist) // 폴더 존재 아이콘
            binding.textviewLinkViewFolder.text = folder                           // 폴더명
        }
    }

    // 링크 View 페이지 아이템 recyclerview 세팅
    private fun initRecycler() {
        linkViewAdapter = LinkViewAdapter(requireContext())
        binding.recyclerviewLinkView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerviewLinkView.adapter = linkViewAdapter
        binding.recyclerviewLinkView.isNestedScrollingEnabled = false // 스크롤을 매끄럽게 해줌
        linkViewAdapter.items = linkViewItems
    }

    // 링크 View 페이지 아이템 추가
    private fun addLinkViewItem(item: LinkViewItem) {
        linkViewItems.apply {
            add(item)
        }
        linkViewAdapter.notifyDataSetChanged()
    }
}