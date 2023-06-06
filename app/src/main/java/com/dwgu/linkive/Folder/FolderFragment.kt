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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFolderBinding.inflate(inflater, container, false)
        val view = binding.root

//        api.login(LoginRequest("sumin", "sumin!")).enqueue(object : Callback<LoginRequest> {
//            override fun onFailure(call: Call<LoginRequest>, t: Throwable) {
//                Log.d("실패", t.message.toString())
//            }
//
//            override fun onResponse(call: Call<LoginRequest>, response: Response<LoginRequest>) {
//                Log.d("성공", response.body().toString())
//                accessToken = response.body()?.id.toString()
//                refreshToken = response.body()?.password.toString()
//            }
//        })
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
            override fun onItemClick(view: View, position: Int, mode: Int, folderNum: Int) {
                if (mode == 0) {
                    // view 모드
                    parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, LinkInFolderFragment())
                        .commit()

                }
                // 편집 모드 새로 만들기
                // x 표시 뜨고 클릭하면 삭제 바텀 시트 뜨게 하기
                else {
                    val bottomSheetFragment = RemoveFolderBottomSheetFragment(folderNum)
                    bottomSheetFragment.setListener(this@FolderFragment)
                    bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
                }
            }
        })
    }

    private fun loadData() {
        // 폴더 리스트 가져오기 api 연동
        api.readFolder("JWT eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6InN1bWluIiwiZW1haWwiOiJzdW1pbkBuYXZlci5jb20iLCJuaWNrbmFtZSI6InN1bWluIiwiaWF0IjoxNjg1NjE2NTAwLCJleHAiOjE2ODU2MjAxMDB9.JGnqSiSnkuSLHG6Pt5YZWiKvacpxsNv_2DpTaPmmdPw", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6InN1bWluIiwiZW1haWwiOiJzdW1pbkBuYXZlci5jb20iLCJuaWNrbmFtZSI6InN1bWluIiwiaWF0IjoxNjg1NjE2NTAwLCJleHAiOjE2ODYyMjEzMDB9.p0NJoSlu62xqrmSn865wbaZLDzTvirmX7gHxwzxPhFI")
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