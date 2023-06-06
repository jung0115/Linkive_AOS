package com.dwgu.linkive.LinkView

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dwgu.linkive.LinkMemoApi.CreateLinkMemo.apiDetailLinkMemo
import com.dwgu.linkive.LinkMemoApi.DetailLinkMemo.LinkMemoBaseInfo
import com.dwgu.linkive.LinkView.LinkViewBottomSheet.ManageLinkBottomFragment
import com.dwgu.linkive.LinkView.LinkViewBottomSheet.SelectPagesheetBottomFragment
import com.dwgu.linkive.LinkView.LinkViewRecycler.*
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.FragmentLinkViewBinding


class LinkViewFragment : Fragment() {

    // ViewBinding Setting
    private var _binding: FragmentLinkViewBinding? = null
    private val binding get() = _binding!!

    // 링크 View 페이지 아이템 Recyclerview
    private var linkViewItems: MutableList<LinkViewItem>? = null
    private lateinit var linkViewAdapter: LinkViewAdapter

    final val URL_OF_LINK_MEMO = "url_of_link_memo"
    final val NUM_OF_LINK_MEMO = "memo_num"
    final val NUM_OF_FOLDER = "folder_num"

    // 링크 URL
    private var linkUrl: String? = null
    // 링크 메모 번호
    private var memoNum: Int? = null
    // 폴더 번호
    private var folderNum: Int = -1

    override fun onResume() {
        super.onResume()

        // 세부 페이지에 보여줄 링크 메모 번호
        memoNum = requireArguments().getInt(NUM_OF_LINK_MEMO)

        // recyclerview 세팅
        initRecycler()

        // server에서 메모 번호로 내용 조회
        apiDetailLinkMemo(
            memoNum!!,
            setLinkViewInfo = {
                setLinkViewInformation(it)
            },
            addLinkViewItem = {
                addLinkViewItem(it)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLinkViewBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 세부 페이지에 보여줄 링크 메모 번호
        memoNum = requireArguments().getInt(NUM_OF_LINK_MEMO)

        // 제목 오른쪽 점 3개 버튼 선택 시 BottomSheet 나오게
        binding.btnLinkViewManage.setOnClickListener {
            val bottomSheet = ManageLinkBottomFragment()

            // 해당 링크 페이지의 url 값, 링크 메모 번호 전달
            val bundle = Bundle()
            bundle.putString(URL_OF_LINK_MEMO, linkUrl)
            bundle.putInt(NUM_OF_LINK_MEMO, memoNum!!)
            bundle.putInt(NUM_OF_FOLDER, folderNum)
            bottomSheet.arguments = bundle

            bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
        }

        // PageSheet 미선택 상태에서 PageSheet 선택 버튼
        binding.btnSelectPagesheet.setOnClickListener {
            val bottomSheet = SelectPagesheetBottomFragment()
            bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
        }
    }

    // 페이지 정보 세팅 - 제목, 폴더, 출처 플랫폼
    private fun setLinkViewInformation(baseInfo: LinkMemoBaseInfo) {
        // 링크 URL
        linkUrl = baseInfo.linkUrl

        // 제목
        binding.textviewLinkViewTitle.text = baseInfo.title

        // 출처 플랫폼
        // 출처 플랫폼이 존재하는 경우
        if(baseInfo.source != null) {
            val iconResourceName = "ic_link_list_item_source_" + baseInfo.source
            val iconResourceId = resources.getIdentifier(iconResourceName, "drawable", requireContext().packageName)
            binding.imgLinkViewSource.setImageResource(iconResourceId)
        }

        // 폴더
        // 폴더가 존재하는 경우
        if(baseInfo.folderNum != null) {
            binding.imgLinkViewFolder.setImageResource(R.drawable.ic_folder_exist) // 폴더 존재 아이콘
            binding.textviewLinkViewFolder.text = baseInfo.folderName              // 폴더명

            // 폴더 번호
            folderNum = baseInfo.folderNum!!
        }

        // PageSheet 미선택 상태이고, 내용이 1개 이하일 때
        if(baseInfo.unselectPageSheet) {
            binding.linearlayoutUnselectPagesheet.visibility = View.VISIBLE
        }
        // 선택 상태일 때
        else {
            binding.linearlayoutUnselectPagesheet.visibility = View.GONE
        }
    }

    // 링크 View 페이지 아이템 recyclerview 세팅
    private fun initRecycler() {
        linkViewItems = mutableListOf<LinkViewItem>()
        linkViewAdapter = LinkViewAdapter(requireContext())
        binding.recyclerviewLinkView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerviewLinkView.adapter = linkViewAdapter
        binding.recyclerviewLinkView.isNestedScrollingEnabled = false // 스크롤을 매끄럽게 해줌
        linkViewAdapter.items = linkViewItems!!
    }

    // 링크 View 페이지 아이템 추가
    private fun addLinkViewItem(item: LinkViewItem) {
        linkViewItems!!.apply {
            add(item)
        }
        linkViewAdapter.notifyDataSetChanged()
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}