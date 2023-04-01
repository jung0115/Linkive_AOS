package com.dwgu.linkive.LinkView.LinkViewBottomSheet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dwgu.linkive.databinding.FragmentDeleteLinkBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// 링크 삭제 BottomSheet
class DeleteLinkBottomFragment : BottomSheetDialogFragment() {
    // ViewBinding Setting
    lateinit var binding: FragmentDeleteLinkBottomBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDeleteLinkBottomBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 취소 버튼 선택 시
        binding.btnCancelDeleteLink.setOnClickListener {
            dismiss()
        }

        // 확인 버튼 선택 시
        binding.btnConfirmDeleteLink.setOnClickListener {
            dismiss()
        }
    }

}