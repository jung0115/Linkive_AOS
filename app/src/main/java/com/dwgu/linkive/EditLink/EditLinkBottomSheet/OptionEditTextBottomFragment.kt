package com.dwgu.linkive.EditLink.EditLinkBottomSheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dwgu.linkive.EditLink.EditLinkOption.EditLinkOptionListener
import com.dwgu.linkive.databinding.FragmentOptionEditTextBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// 링크 편집 페이지 > 글 아이템 옵션 BottomSheet
class OptionEditTextBottomFragment : BottomSheetDialogFragment() {

    // ViewBinding Setting
    lateinit var binding: FragmentOptionEditTextBottomBinding

    // 리사이클러뷰에서의 position 값
    final val POSITION_IN_RECYCLERVIEW = "position_in_recyclerview"

    var position: String? = null

    // 글 초기화 event 전달하기 위한 listener
    private lateinit var resetItemListener: EditLinkOptionListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            resetItemListener = context as EditLinkOptionListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                context.toString()
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOptionEditTextBottomBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 리사이클러뷰에서의 position 값
        position = arguments?.getString(POSITION_IN_RECYCLERVIEW)

        // 내용 초기화 버튼
        binding.relativelayoutSetText.setOnClickListener(View.OnClickListener {
            dismiss()

            // 글 내용 초기화
            resetItemListener.resetItemListener(position!!.toInt(), "text")
        })

        // 아이템 삭제 버튼 선택 시 -> 삭제 확인 BottomSheet
        binding.relativelayoutDeleteEditItem.setOnClickListener(View.OnClickListener {
            dismiss()

            // 링크 편집 아이템 삭제 확인 BottomSheet 열기
            val bottomSheet = DeleteEditItemBottomFragment()

            // recycleview에서의 position 값 전달
            val bundle = Bundle()
            bundle.putString("position_in_recyclerview", position)
            bottomSheet.arguments = bundle

            bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
        })
    }
}