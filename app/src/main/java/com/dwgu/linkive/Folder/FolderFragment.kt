package com.dwgu.linkive.Folder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.dwgu.linkive.Folder.sortFolder.sortFolderAdapter
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.FragmentFolderBinding
import kotlinx.android.synthetic.main.fragment_folder.*

class FolderFragment : Fragment() {
    private var _binding: FragmentFolderBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFolderBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 폴더 정렬 스피너 어댑터 연결
//        val sortFolderAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.item_sort_folder, android.R.layout.simple_spinner_dropdown_item)
//        binding.spinnerSortFolder.adapter = sortFolderAdapter
        val data: ArrayList<String> = arrayListOf()
        data.add("생성순")
        data.add("가나다순")
        val sortFolderAdapter = sortFolderAdapter(requireContext(), data)
        binding.spinnerSortFolder.adapter = sortFolderAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}