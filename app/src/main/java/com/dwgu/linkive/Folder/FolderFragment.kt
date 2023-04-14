package com.dwgu.linkive.Folder

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import com.dwgu.linkive.Folder.FolderListAdapter.FolderListAdapter
import com.dwgu.linkive.Folder.FolderListAdapter.FolderListItem
import com.dwgu.linkive.Folder.SortFolder.SortFolderAdapter
import com.dwgu.linkive.databinding.FragmentFolderBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDateTime

class FolderFragment : Fragment() {
    private var _binding: FragmentFolderBinding? = null
    private val binding get() = _binding!!

    //폴더 리스트
    private val folderOfList = arrayListOf<FolderListItem>()

    // 폴더 정렬 스피너 어댑터
    private lateinit var sortFolderAdapter: SortFolderAdapter
    // 폴더 리사이클러뷰 어댑터
    private lateinit var folderListAdapter: FolderListAdapter

    // 폴더 관리 바텀 시트
    val bottomSheetFragment = FolderMenuBottomSheetFragment()
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 폴더 정렬 스피너 어댑터 연결
//        val sortFolderAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.item_sort_folder, android.R.layout.simple_spinner_dropdown_item)
//        binding.spinnerSortFolder.adapter = sortFolderAdapter
        val data: ArrayList<String> = arrayListOf()
        data.add("생성순")
        data.add("가나다순")
        sortFolderAdapter = SortFolderAdapter(requireContext(), data)
        binding.spinnerSortFolder.adapter = sortFolderAdapter


        // 폴더리스트 데이터 추가
        addFolder("놀이공원")
        addFolder("제주")
        addFolder("진주")
        addFolder("부산")

        // 폴더리스트 어댑터 연결
        binding.recyclerviewFolderList.layoutManager = GridLayoutManager(requireContext(), 2)
        folderListAdapter = FolderListAdapter(folderOfList)
        binding.recyclerviewFolderList.adapter = folderListAdapter


        // 메뉴 버튼 클릭 시 FolderMenuBottomSheet 띄움
        binding.btnFolderMenu.setOnClickListener {
            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
        }


        // 정렬 스피너의 값이 바뀌면
        binding.spinnerSortFolder.onItemSelectedListener = object :
        AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //생성순 선택 시
                if(position==0) {
                    folderOfList.sortBy { it.createdDate }
                    folderListAdapter.notifyDataSetChanged()
                }
                //가나다순 선택 시
                else if(position==1) {
                    folderOfList.sortBy { it.name }
                    folderListAdapter.notifyDataSetChanged()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

    }

    //폴더 추가 메소드
    @RequiresApi(Build.VERSION_CODES.O)
    private fun addFolder(name: String){
        val item = FolderListItem(name, createdDate = LocalDateTime.now())
        folderOfList.add(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}