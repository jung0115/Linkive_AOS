package com.dwgu.linkive.Folder

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.dwgu.linkive.Folder.FolderApi.ReadFoldersList
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.FragmentAddFolderBottomSheetBinding
import com.dwgu.linkive.databinding.FragmentEditFolderCoverBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class EditFolderCoverFragment(private val folder: ReadFoldersList.ReadFoldersResponse) : BottomSheetDialogFragment() {

    private var _binding: FragmentEditFolderCoverBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditFolderCoverBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 링크 커버 중에서 선택하기 버튼 클릭 시
        binding.layoutSelectInLinkCover.setOnClickListener {
            val bottomSheetFragment = SelectFolderCoverFragment(folder)
            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
            dismiss()
        }

        // 취소 버튼
        binding.btnCancel.setOnClickListener {
            val bottomSheetFragment = LinkInFolderMenuBottomSheetFragment(folder)
            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
            dismiss()
        }
        // 확인 버튼
        binding.btnConfirm.setOnClickListener {
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