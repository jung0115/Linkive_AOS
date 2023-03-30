package com.dwgu.linkive.EditLink.EditLinkBottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dwgu.linkive.databinding.FragmentNoticeNotSaveBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// 링크 편집 페이지에서 이전 버튼 누르면 저장 안 된다고 알려주는 BottomSheet
class NoticeNotSaveBottomFragment : BottomSheetDialogFragment() {

    // ViewBinding Setting
    lateinit var binding: FragmentNoticeNotSaveBottomBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoticeNotSaveBottomBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 취소 버튼 선택 시
        binding.btnCancelBackPage.setOnClickListener {
            dismiss()
        }

        // 확인 버튼 선택 시
        binding.btnConfirmBackPage.setOnClickListener {
            dismiss()

            // 페이지 닫기
            requireActivity().finish()
        }
    }
}