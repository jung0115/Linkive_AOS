package com.dwgu.linkive.EditLink.EditLinkBottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dwgu.linkive.databinding.FragmentOptionEditPlaceBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// 링크 편집 페이지 > 장소(위치) 아이템 옵션 BottomSheet
class OptionEditPlaceBottomFragment : BottomSheetDialogFragment() {

    // ViewBinding Setting
    lateinit var binding: FragmentOptionEditPlaceBottomBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOptionEditPlaceBottomBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 장소 설정/변경 버튼 선택 시
        binding.relativelayoutSetPlace.setOnClickListener(View.OnClickListener {
            dismiss()

            // 주소 입력 api 열어서 이미지 선택
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