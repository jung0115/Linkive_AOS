package com.dwgu.linkive.EditLink.EditLinkBottomSheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dwgu.linkive.EditLink.EditLinkOption.EditLinkOptionListener
import com.dwgu.linkive.databinding.FragmentDeleteEditItemBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.ClassCastException

// 링크 편집 아이템 삭제 확인 BottomSheet
class DeleteEditItemBottomFragment : BottomSheetDialogFragment() {
    // ViewBinding Setting
    lateinit var binding: FragmentDeleteEditItemBottomBinding

    // 리사이클러뷰에서의 position 값
    final val POSITION_IN_RECYCLERVIEW = "position_in_recyclerview"

    var position: String? = null

    // 삭제 확인 event 전달하기 위한 listener
    private lateinit var deleteListener: EditLinkOptionListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            deleteListener = context as EditLinkOptionListener
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
        binding = FragmentDeleteEditItemBottomBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 리사이클러뷰에서의 position 값
        position = arguments?.getString(POSITION_IN_RECYCLERVIEW)

        // 삭제 취소
        binding.btnCancelDeleteEditItem.setOnClickListener {
            dismiss()
        }

        // 삭제 확인
        binding.btnConfirmDeleteEditItem.setOnClickListener {
            dismiss()

            // 삭제할 아이템 정보 전달
            deleteListener.deleteItemListener(position!!.toInt())
        }
    }

}