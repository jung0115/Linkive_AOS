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
import com.dwgu.linkive.Api.ApiClient
import com.dwgu.linkive.Folder.FolderApi.*
import com.dwgu.linkive.Folder.FolderListAdapter.FolderListAdapter
import com.dwgu.linkive.Folder.SortFolder.SortFolderAdapter
import com.dwgu.linkive.Login.GloabalApplication
import com.dwgu.linkive.R

import com.dwgu.linkive.databinding.FragmentFolderBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FolderFragment : Fragment(), SetFolderListListener {
    private var _binding: FragmentFolderBinding? = null
    private val binding get() = _binding!!

    //retrofit builder 선언
    private val retrofit = ApiClient.getInstance()
    private val api: FolderInterface = retrofit.create(FolderInterface::class.java)

    //폴더 리스트
    private var folderOfList = arrayListOf<ReadFoldersList.ReadFoldersResponse>()

    // 폴더 정렬 스피너 어댑터
    private lateinit var sortFolderAdapter: SortFolderAdapter
    // 폴더 리사이클러뷰 어댑터
    private lateinit var folderListAdapter: FolderListAdapter

    // 토큰 값
    private lateinit var accessToken: String
    private lateinit var refreshToken: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accessToken = "JWT ${GloabalApplication.prefs.getString("accessToken", "")}"
        refreshToken = GloabalApplication.prefs.getString("refreshToken", "")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFolderBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    fun setFolderAdapter(List: ArrayList<ReadFoldersList.ReadFoldersResponse>, mode: Int = 0){
        // 폴더리스트 어댑터 연결
        binding.recyclerviewFolderList.layoutManager = GridLayoutManager(requireContext(), 2)
        Log.d("folderOfList 연결", List.toString())
        folderListAdapter = FolderListAdapter(requireContext(), List, mode)
        binding.recyclerviewFolderList.adapter = folderListAdapter

        // 폴더 클릭시 폴더 내부 페이지
        folderListAdapter.setOnItemclickListner(object: FolderListAdapter.OnItemClickListner{
            override fun onItemClick(view: View, position: Int, mode: Int, folder: ReadFoldersList.ReadFoldersResponse) {
                if (mode == 0) {
                    // view 모드
                    // password가 있으면 check dialog
                    if (folder.isLocked){
                        val checkPasswordFragment = CheckFolderPasswordFragment(folder)
                        checkPasswordFragment.show(parentFragmentManager, checkPasswordFragment.tag)
                    }
                    else {
                        parentFragmentManager
                            .beginTransaction()
                            .add(R.id.nav_host_fragment, LinkInFolderFragment(folder))
                            .commit()
                    }


                }
                // 편집 모드 새로 만들기
                // x 표시 뜨고 클릭하면 삭제 바텀 시트 뜨게 하기
                else {
                    // mode : "out" -> 폴더 외부에서 remove 호출
                    val bottomSheetFragment = RemoveFolderBottomSheetFragment(folder, "out")
                    bottomSheetFragment.setListener(this@FolderFragment)
                    bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
                }
            }
        })
    }

    private fun loadData() {
        // 폴더 리스트 가져오기 api 연동
        api.readFolder(accessToken, refreshToken)
            .enqueue(object: Callback<ReadFoldersList> {
                override fun onFailure(call: Call<ReadFoldersList>, t: Throwable) {
                    Log.d("실패", t.message.toString())
                }
                override fun onResponse(call: Call<ReadFoldersList>, response: Response<ReadFoldersList>) {
                    response.takeIf { it.isSuccessful }
                        ?.body()
                        ?.let { it ->
                            folderOfList = it.folderList as ArrayList<ReadFoldersList.ReadFoldersResponse>
                            setFolderAdapter(folderOfList)
                        }
                }
            })
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        폴더 어댑터 연결
        setFolderAdapter(folderOfList)

        // 폴더 정렬 스피너 어댑터 연결
        val data: ArrayList<String> = arrayListOf()
        data.add("생성순")
        data.add("가나다순")
        sortFolderAdapter = SortFolderAdapter(requireContext(), data)
        binding.spinnerSortFolder.adapter = sortFolderAdapter

        // 폴더 리스트 가져오기
        loadData()

        // 메뉴 버튼 클릭 시 FolderMenuBottomSheet 띄움
        binding.btnFolderMenu.setOnClickListener {
            // 폴더 관리 바텀 시트
            val bottomSheetFragment = FolderMenuBottomSheetFragment()
            // 폴더 추가 및 삭제 바텀 시트에서 loadData()를 사용하기 위함
            bottomSheetFragment.setListener(this)
            bottomSheetFragment.show(requireActivity().supportFragmentManager, bottomSheetFragment.tag)
        }

        // 정렬 스피너의 값이 바뀌면
        binding.spinnerSortFolder.onItemSelectedListener = object :
        AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //생성순 선택 시
                if(position==0) {
                    folderOfList.sortBy { it.folderNum }
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun setFolderList() {
        loadData()
    }

    override fun setRemove() {
        setFolderAdapter(folderOfList, 1)
    }
}