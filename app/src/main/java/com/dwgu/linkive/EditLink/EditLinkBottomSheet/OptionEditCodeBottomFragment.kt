package com.dwgu.linkive.EditLink.EditLinkBottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dwgu.linkive.databinding.FragmentOptionEditCodeBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// 링크 편집 페이지 > 코드 아이템 옵션 BottomSheet
class OptionEditCodeBottomFragment : BottomSheetDialogFragment() {

    // ViewBinding Setting
    lateinit var binding: FragmentOptionEditCodeBottomBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOptionEditCodeBottomBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 내용 초기화 버튼
        binding.relativelayoutSetCode.setOnClickListener(View.OnClickListener {
            dismiss()

            // 글 내용 초기화
        })

        // 아이템 삭제 버튼 선택 시 -> 삭제 확인 BottomSheet
        binding.relativelayoutDeleteEditItem.setOnClickListener(View.OnClickListener {
            dismiss()

            // 링크 편집 아이템 삭제 확인 BottomSheet 열기
            val bottomSheet = DeleteEditItemBottomFragment()
            bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
        })
    }
}