package com.dwgu.linkive.Folder

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dwgu.linkive.databinding.FragmentFolderMenuBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class FolderMenuBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentFolderMenuBottomSheetBinding? = null
    private val binding get() = _binding!!

    // 폴더리스트 불러오는 리스너
    private lateinit var setFolderListListener: SetFolderListListener

    //편집모드 리스너
//    private lateinit var removeFolderListener: RemoveFolderListener

    // 폴더 리스트 리스너 초기화
    fun setListener(listener: SetFolderListListener) {
        setFolderListListener = listener
    }

    //편집모드 리스너
//    fun setRemoveFolderListener(listener: RemoveFolderListener) {
//        removeFolderListener = listener
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFolderMenuBottomSheetBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme).apply {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.isDraggable = false
        }
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 추가하기 버튼 클릭 시 추가 바텀 시트
        binding.layoutAddFolder.setOnClickListener {
            val bottomSheetFragment = AddFolderBottomSheetFragment()
            bottomSheetFragment.setListener(setFolderListListener)
            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
            dismiss()
        }

        // 삭제하기 버튼 클릭 시 삭제 바텀 시트
        binding.layoutRemoveFolder.setOnClickListener {
            setFolderListListener.setRemove()
//            removeFolderListener.setRemove()
//            val bottomSheetFragment = RemoveFolderBottomSheetFragment()
//            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
            dismiss()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}