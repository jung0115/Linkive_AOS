package com.dwgu.linkive.Folder

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.FragmentFolderBinding
import com.dwgu.linkive.databinding.FragmentFolderMenuBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class FolderMenuBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentFolderMenuBottomSheetBinding? = null
    private val binding get() = _binding!!

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

        binding.layoutAddFolder.setOnClickListener {
            val bottomSheetFragment = AddFolderBottomSheetFragment()
            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}