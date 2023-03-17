package com.dwgu.linkive.Folder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.dwgu.linkive.Folder.FolderListAdapter.FolderListAdapter
import com.dwgu.linkive.Folder.FolderListAdapter.FolderListItem
import com.dwgu.linkive.Folder.SortFolder.SortFolderAdapter
import com.dwgu.linkive.databinding.FragmentFolderBinding

class FolderFragment : Fragment() {
    private var _binding: FragmentFolderBinding? = null
    private val binding get() = _binding!!

    //폴더 리스트
    private val folderOfList = arrayListOf<FolderListItem>()

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
        val sortFolderAdapter = SortFolderAdapter(requireContext(), data)
        binding.spinnerSortFolder.adapter = sortFolderAdapter


        // 폴더리스트 데이터 추가
        addFolder("놀이공원")
        addFolder("제주")
        addFolder("진주")
        // 폴더리스트 어댑터 연결
        binding.recyclerviewFolderList.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerviewFolderList.adapter = FolderListAdapter(folderOfList)
    }

    //폴더 추가 메소드
    fun addFolder(name: String){
        val item = FolderListItem(name)
        folderOfList.add(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}