package com.dwgu.linkive.EditLink.EditLinkBottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dwgu.linkive.databinding.FragmentDeleteEditItemBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// 링크 편집 아이템 삭제 확인 BottomSheet
class DeleteEditItemBottomFragment : BottomSheetDialogFragment() {
    // ViewBinding Setting
    lateinit var binding: FragmentDeleteEditItemBottomBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDeleteEditItemBottomBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}