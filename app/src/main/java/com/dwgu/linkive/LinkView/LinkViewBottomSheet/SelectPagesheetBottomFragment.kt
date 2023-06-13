package com.dwgu.linkive.LinkView.LinkViewBottomSheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dwgu.linkive.EditLink.EditLinkActivity
import com.dwgu.linkive.LinkMemoApi.CreateLinkMemo.apiGetAllPageSheet
import com.dwgu.linkive.LinkMemoApi.GetAllPageSheet.GetPageSheetData
import com.dwgu.linkive.LinkView.SelectPagesheetRecycler.SelectPagesheetAdapter
import com.dwgu.linkive.LinkView.SelectPagesheetRecycler.SelectPagesheetItem
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.FragmentSelectPagesheetBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// PageSheet 선택 BottomSheet
class SelectPagesheetBottomFragment : BottomSheetDialogFragment() {

    // ViewBinding Setting
    lateinit var binding: FragmentSelectPagesheetBottomBinding

    // PageSheet 선택 BottomSheet 폴더명 아이템 Recyclerview
    private val selectDefaultPagesheetItems = mutableListOf<SelectPagesheetItem>()
    private lateinit var selectDefaultPagesheetAdapter: SelectPagesheetAdapter
    private val selectCustomPagesheetItems = mutableListOf<SelectPagesheetItem>()
    private lateinit var selectCustomPagesheetAdapter: SelectPagesheetAdapter

    // server에서 받아온 PageSheet
    private var allPagesheet: MutableList<GetPageSheetData>? = null

    final val NUM_OF_LINK_MEMO = "memo_num"
    final val NUM_OF_PAGESHEET = "pagesheet_num"

    // 링크 메모 번호
    private var memoNum: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectPagesheetBottomBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 세부 페이지에 보여줄 링크 메모 번호
        memoNum = requireArguments().getInt(NUM_OF_LINK_MEMO)

        // recyclerview 세팅
        initRecycler()

        // 페이지시트 전체 조회
        apiGetAllPageSheet(
            setAllpageSheet = {
                setAllPageSheet(it)
            }
        )

        // 취소 버튼 선택 시
        binding.btnCancelSelectPagesheet.setOnClickListener {
            dismiss()
        }

        // 확인 버튼 선택 시
        binding.btnConfirmSelectPagesheet.setOnClickListener {
            dismiss()

            // 선택된 PageSheet layout
            var selectPageSheetNum = getSelectPageSheetNum()

            // 링크 편집 페이지 열기
            // 선택한 PageSheet 정보 넘겨줘야 함
            val editLinkActivity = Intent(requireContext(), EditLinkActivity::class.java)
            editLinkActivity.putExtra(NUM_OF_LINK_MEMO, memoNum)
            editLinkActivity.putExtra(NUM_OF_PAGESHEET, selectPageSheetNum)
            startActivity(editLinkActivity)
        }
    }

    // PageSheet 선택 아이템 recyclerview 세팅
    private fun initRecycler() {
        // 기본 PageSheet
        selectDefaultPagesheetAdapter = SelectPagesheetAdapter(
            requireContext(),
            setAllUncheck = {
                setAllPagesheetUncheck()
            }
        )
        binding.recyclerviewSelectPagesheetDefault.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerviewSelectPagesheetDefault.adapter = selectDefaultPagesheetAdapter
        binding.recyclerviewSelectPagesheetDefault.isNestedScrollingEnabled = false // 스크롤을 매끄럽게 해줌
        selectDefaultPagesheetAdapter.items = selectDefaultPagesheetItems

        // Custom PageSheet
        selectCustomPagesheetAdapter = SelectPagesheetAdapter(
            requireContext(),
            setAllUncheck = {
                setAllPagesheetUncheck()
            }
        )
        binding.recyclerviewSelectPagesheetCustom.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerviewSelectPagesheetCustom.adapter = selectCustomPagesheetAdapter
        binding.recyclerviewSelectPagesheetCustom.isNestedScrollingEnabled = false // 스크롤을 매끄럽게 해줌
        selectCustomPagesheetAdapter.items = selectCustomPagesheetItems
    }

    // 페이지시트 전체 조회 후 데이터 적용
    private fun setAllPageSheet(pagesheets: MutableList<GetPageSheetData>) {

        allPagesheet = pagesheets

        // PageSheet
        // 자유
        selectDefaultPagesheetItems.add(SelectPagesheetItem(-1, getString(R.string.pagesheet_free), true))

        for(pageSheet in pagesheets) {
            // 기본 PageSheet
            if(pageSheet.pagesheet_num >= 1 && pageSheet.pagesheet_num <= 5) {
                selectDefaultPagesheetItems.add(SelectPagesheetItem(pageSheet.pagesheet_num, pageSheet.name))
            }
            // 커스텀 PageSheet
            else {
                selectCustomPagesheetItems.add(SelectPagesheetItem(pageSheet.pagesheet_num, pageSheet.name))
            }
        }

        // Custom PageSheet 아예 없으면 라벨 지우기!
        if(selectCustomPagesheetItems.size == 0) {
            binding.lineOfCustom1.visibility = View.GONE
            binding.lineOfCustom2.visibility = View.GONE
            binding.textCustumLabel.visibility = View.GONE
        }

        selectDefaultPagesheetAdapter.notifyDataSetChanged()
        selectCustomPagesheetAdapter.notifyDataSetChanged()
    }

    // 전체 선택 해제
    private fun setAllPagesheetUncheck() {
        for(ckbox in selectDefaultPagesheetItems) {
            ckbox.selectPagesheet = false
        }
        for(ckbox in selectCustomPagesheetItems) {
            ckbox.selectPagesheet = false
        }

        selectDefaultPagesheetAdapter.notifyDataSetChanged()
        selectCustomPagesheetAdapter.notifyDataSetChanged()
    }

    // 선택된 PageSheet 번호
    private fun getSelectPageSheetNum(): Int {
        var pageSheetNum = -1
        for(pagesheet in selectDefaultPagesheetItems) {
            if(pagesheet.selectPagesheet) {
                pageSheetNum = pagesheet.pagesheetNum
                break
            }
        }
        for(pagesheet in selectCustomPagesheetItems) {
            if(pagesheet.selectPagesheet) {
                pageSheetNum = pagesheet.pagesheetNum
                break
            }
        }

        return pageSheetNum
    }
}