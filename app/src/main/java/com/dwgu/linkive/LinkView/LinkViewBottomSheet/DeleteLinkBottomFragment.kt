package com.dwgu.linkive.LinkView.LinkViewBottomSheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dwgu.linkive.LinkMemoApi.CreateLinkMemo.apiDeleteLinkMemo
import com.dwgu.linkive.LinkView.LinkViewMenuListener.LinkViewMenuListener
import com.dwgu.linkive.databinding.FragmentDeleteLinkBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// 링크 삭제 BottomSheet
class DeleteLinkBottomFragment : BottomSheetDialogFragment() {
    // ViewBinding Setting
    lateinit var binding: FragmentDeleteLinkBottomBinding

    // 링크 메모 번호
    final val NUM_OF_LINK_MEMO = "memo_num"
    private var memoNum: Int? = null

    // 삭제 확인 event 전달하기 위한 listener
    private lateinit var deleteLinkMemoListener: LinkViewMenuListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            deleteLinkMemoListener = context as LinkViewMenuListener
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
        binding = FragmentDeleteLinkBottomBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 링크 메모 번호
        memoNum = requireArguments().getInt(NUM_OF_LINK_MEMO)

        // 취소 버튼 선택 시
        binding.btnCancelDeleteLink.setOnClickListener {
            dismiss()
        }

        // 확인 버튼 선택 시
        binding.btnConfirmDeleteLink.setOnClickListener {
            // 링크 메모 삭제
            apiDeleteLinkMemo(memoNum!!)

            // 삭제 확인 bottomSheet 닫기
            dismiss()

            // 링크 메모 삭제 후 링크 세부 페이지 닫기
            deleteLinkMemoListener.backStackListener()
        }
    }

}