package com.dwgu.linkive.LinkView.LinkViewBottomSheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dwgu.linkive.EditLink.EditLinkActivity
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectPagesheetBottomBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Custom PageSheet 아예 없으면 라벨 지우기!

        // recyclerview 세팅
        initRecycler()

        // 샘플 데이터
        addSelectCustomPagesheetItem(SelectPagesheetItem("커스텀1"))
        addSelectCustomPagesheetItem(SelectPagesheetItem("커스텀2"))

        // 취소 버튼 선택 시
        binding.btnCancelSelectPagesheet.setOnClickListener {
            dismiss()
        }

        // 확인 버튼 선택 시
        binding.btnConfirmSelectPagesheet.setOnClickListener {
            dismiss()

            // 링크 편집 페이지 열기
            // 선택한 PageSheet 정보 넘겨줘야 함
            val editLinkActivity = Intent(requireContext(), EditLinkActivity::class.java)
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

        // 기본 PageSheet 데이터 넣기
        selectDefaultPagesheetItems.add(SelectPagesheetItem(getString(R.string.pagesheet_free), true))
        selectDefaultPagesheetItems.add(SelectPagesheetItem(getString(R.string.pagesheet_trip)))
        selectDefaultPagesheetItems.add(SelectPagesheetItem(getString(R.string.pagesheet_study)))
        selectDefaultPagesheetItems.add(SelectPagesheetItem(getString(R.string.pagesheet_programming)))
        selectDefaultPagesheetItems.add(SelectPagesheetItem(getString(R.string.pagesheet_diary)))
        selectDefaultPagesheetItems.add(SelectPagesheetItem(getString(R.string.pagesheet_checklist)))
        selectDefaultPagesheetAdapter.notifyDataSetChanged()
    }

    // Custom PageSheet 선택 아이템 추가
    private fun addSelectCustomPagesheetItem(item: SelectPagesheetItem) {
        selectCustomPagesheetItems.apply {
            add(item)
        }
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

}