package com.dwgu.linkive.LinkView.LinkViewBottomSheet

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dwgu.linkive.EditLink.EditLinkActivity
import com.dwgu.linkive.databinding.FragmentManageLinkBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// 링크 관리 BottomSheet
class ManageLinkBottomFragment : BottomSheetDialogFragment() {

    // ViewBinding Setting
    lateinit var binding: FragmentManageLinkBottomBinding

    // 해당 링크 페이지의 url 값 전달
    final val URL_OF_LINK_MEMO = "url_of_link_memo"

    var linkUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentManageLinkBottomBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 해당 링크 페이지의 url 값
        linkUrl = arguments?.getString(URL_OF_LINK_MEMO)

        // 페이지 수정 버튼 선택 시 -> 링크 편집 페이지
        binding.relativelayoutEditPage.setOnClickListener(View.OnClickListener {
            // 현재 BottomSheet 닫기
            dismiss()

            // 링크 편집 페이지 열기
            val editLinkActivity = Intent(requireContext(), EditLinkActivity::class.java)
            startActivity(editLinkActivity)
        })

        // 폴더 이동 버튼 선택 시 -> 폴더 선택 BottomSheet
        binding.relativelayoutMoveFolder.setOnClickListener(View.OnClickListener {
            // 현재 BottomSheet 닫기
            dismiss()

            // 폴더 선택 BottomSheet 열기
            val bottomSheet = MoveFolderBottomFragment()
            bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
        })

        // 링크로 접속하기 버튼 선택 시 -> 해당 url을 브라우저에서 실행
        binding.relativelayoutGotoUrl.setOnClickListener(View.OnClickListener {
            // 현재 BottomSheet 닫기
            dismiss()

            // url을 브라우저 앱에서 실행
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkUrl))
            startActivity(intent)
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