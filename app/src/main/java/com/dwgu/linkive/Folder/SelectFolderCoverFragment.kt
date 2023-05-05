package com.dwgu.linkive.Folder

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.Toast
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.FragmentLinkInFolderMenuBottomSheetBinding
import com.dwgu.linkive.databinding.FragmentSelectFolderCoverBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SelectFolderCoverFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentSelectFolderCoverBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectFolderCoverBinding.inflate(inflater, container, false)
        val view = binding.root

        dialog?.setCanceledOnTouchOutside(false) // 바텀시트의 바깥 영역을 클릭해도 dismiss 되지 않게 함
//        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 다이얼로그 바깥 배경 그림자 없애기
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        // 다이얼로그 밖 view 터치할 수 있게 하기
        dialog?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 취소 버튼
        binding.btnCancel.setOnClickListener {
            val bottomSheetFragment = LinkInFolderMenuBottomSheetFragment()
            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
            dismiss()
        }
        // 확인 버튼
        binding.btnConfirm.setOnClickListener {
            dismiss()
        }


    }

//    override fun onResume() {
//        super.onResume()
//        dialog?.setOnShowListener {
//            val bottomSheetFragment = (dialog as? BottomSheetDialog)?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
//            bottomSheetFragment?.setOnClickListener{
//                Toast.makeText(context, "Clicked outside", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme).apply {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.isDraggable = false
        }
        dialog.setOnShowListener {
            val bottomSheet = dialog.findViewById<FrameLayout>(R.id.view_folder_name)
            bottomSheet?.setOnClickListener{
                Toast.makeText(context, "Clicked outside", Toast.LENGTH_SHORT).show()
            }
        }
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}