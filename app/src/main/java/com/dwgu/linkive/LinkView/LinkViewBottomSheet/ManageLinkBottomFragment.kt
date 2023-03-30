package com.dwgu.linkive.LinkView.LinkViewBottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dwgu.linkive.EditLink.EditLinkFragment
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.FragmentManageLinkBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// 링크 관리 BottomSheet
class ManageLinkBottomFragment : BottomSheetDialogFragment() {

    // ViewBinding Setting
    lateinit var binding: FragmentManageLinkBottomBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentManageLinkBottomBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 페이지 수정 버튼 선택 시 -> 링크 편집 페이지
        binding.relativelayoutEditPage.setOnClickListener(View.OnClickListener {
            // 현재 BottomSheet 닫기
            dismiss()

            // 링크 편집 페이지 열기
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .add(R.id.nav_host_fragment, EditLinkFragment())
                .commit()
        })

        // 폴더 이동 버튼 선택 시 -> 폴더 선택 BottomSheet
        binding.relativelayoutMoveFolder.setOnClickListener(View.OnClickListener {
            // 현재 BottomSheet 닫기
            dismiss()

            // 폴더 선택 BottomSheet 열기
            val bottomSheet = MoveFolderBottomFragment()
            bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
        })

        // 삭제하기 버튼 선택 시 -> 링크 삭제 확인 BottomSheet
        binding.relativelayoutDeletePage.setOnClickListener(View.OnClickListener {
            // 현재 BottomSheet 닫기
            dismiss()

            // 링크 삭제 확인 BottomSheet 열기
            val bottomSheet = DeleteLinkBottomFragment()
            bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
        })
    }
}