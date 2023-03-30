package com.dwgu.linkive.EditLink.EditLinkBottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dwgu.linkive.databinding.FragmentOptionEditTextBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// 링크 편집 페이지 > 글 아이템 옵션 BottomSheet
class OptionEditTextFragment : BottomSheetDialogFragment() {

    // ViewBinding Setting
    lateinit var binding: FragmentOptionEditTextBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOptionEditTextBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}