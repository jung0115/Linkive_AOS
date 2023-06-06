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

    // 해당 링크 페이지의 url, 메모 번호, 폴더 번호 값 전달
    final val URL_OF_LINK_MEMO = "url_of_link_memo"
    final val NUM_OF_LINK_MEMO = "memo_num"
    final val NUM_OF_FOLDER = "folder_num"

    // 링크 URL
    private var linkUrl: String? = null
    // 링크 메모 번호
    private var memoNum: Int? = null
    // 폴더 번호
    private var folderNum: Int? = null

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
        linkUrl = requireArguments().getString(URL_OF_LINK_MEMO)

        // 링크 메모 번호
        memoNum = requireArguments().getInt(NUM_OF_LINK_MEMO)

        // 링크 폴더 번호
        folderNum = requireArguments().getInt(NUM_OF_FOLDER)

        // 페이지 수정 버튼 선택 시 -> 링크 편집 페이지
        binding.relativelayoutEditPage.setOnClickListener(View.OnClickListener {
            // 현재 BottomSheet 닫기
            dismiss()

            // 링크 편집 페이지 열기
            val editLinkActivity = Intent(requireContext(), EditLinkActivity::class.java)
            editLinkActivity.putExtra(NUM_OF_LINK_MEMO, memoNum)
            startActivity(editLinkActivity)
        })

        // 폴더 이동 버튼 선택 시 -> 폴더 선택 BottomSheet
        binding.relativelayoutMoveFolder.setOnClickListener(View.OnClickListener {
            // 현재 BottomSheet 닫기
            dismiss()

            // 폴더 선택 BottomSheet 열기
            val bottomSheet = MoveFolderBottomFragment()
            // 링크 메모 번호, 폴더 번호 전달
            val bundle = Bundle()
            bundle.putInt(NUM_OF_LINK_MEMO, memoNum!!)
            bundle.putInt(NUM_OF_FOLDER, folderNum!!)
            bottomSheet.arguments = bundle
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
            // 링크 메모 번호 전달
            val bundle = Bundle()
            bundle.putInt(NUM_OF_LINK_MEMO, memoNum!!)
            bottomSheet.arguments = bundle
            bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
        })
    }
}