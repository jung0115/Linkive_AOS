package com.dwgu.linkive.Folder

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dwgu.linkive.Folder.FolderApi.ReadFoldersList
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.FragmentAddFolderBottomSheetBinding
import com.dwgu.linkive.databinding.FragmentLinkInFolderMenuBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LinkInFolderMenuBottomSheetFragment(private val folder: ReadFoldersList.ReadFoldersResponse) : BottomSheetDialogFragment() {

    private var _binding: FragmentLinkInFolderMenuBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLinkInFolderMenuBottomSheetBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 폴더 썸네일 변경 버튼 클릭 시 추가 바텀 시트
        binding.layoutFolderThumbnailEdit.setOnClickListener {
            val bottomSheetFragment = EditFolderCoverFragment(folder)
            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
            dismiss()
        }

        // 폴더 비밀번호 설정 / 변경 버튼 클릭 시
        // 기존 비밀번호가 없다면 비밀번호 설정으로
        binding.layoutFolderPasswordSettingEdit.setOnClickListener {
            val bottomSheetFragment = EditFolderPasswordFragment(folder)
            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
            dismiss()
        }

        // 비밀번호가 있다면 비밀번호 변경으로



        // 삭제하기 버튼 클릭 시
        binding.layoutRemoveFolder.setOnClickListener {

            // 모드 : in -> 폴더 내부에서 remove를 호출
            val bottomSheetFragment = RemoveFolderBottomSheetFragment(folder, "in")
            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
            dismiss()
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme).apply {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.isDraggable = false
        }
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}