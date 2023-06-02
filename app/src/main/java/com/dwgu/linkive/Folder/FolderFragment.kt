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

class FolderFragment : Fragment() {
    private var _binding: FragmentFolderBinding? = null
    private val binding get() = _binding!!

    //retrofit builder 선언
    private val retrofit = ApiClient.getInstance()
    private val api: FolderInterface = retrofit.create(FolderInterface::class.java)

//    private var accessToken: String? = null
//    private var refreshToken: String? = null

    //폴더 리스트
    private var folderOfList = arrayListOf<ReadFoldersResponse>()

    // 폴더 정렬 스피너 어댑터
    private lateinit var sortFolderAdapter: SortFolderAdapter
    // 폴더 리사이클러뷰 어댑터
    private lateinit var folderListAdapter: FolderListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFolderBinding.inflate(inflater, container, false)
        val view = binding.root

//        api.signUp(SignUpRequest("sumin", "sumin!", "sumin@naver.com", "sumin")).enqueue(object : Callback<String> {
//            override fun onFailure(call: Call<String>, t: Throwable) {
//                Log.d("실패", t.message.toString())
//            }
//
//            override fun onResponse(call: Call<String>, response: Response<String>) {
//                Log.d("성공", response.body().toString())
//            }
//        })



//        api.login(LoginRequest("sumin", "sumin!")).enqueue(object : Callback<LoginRequest> {
//            override fun onFailure(call: Call<LoginRequest>, t: Throwable) {
//                Log.d("실패", t.message.toString())
//            }
//
//            override fun onResponse(call: Call<LoginRequest>, response: retrofit2.Response<LoginRequest>) {
//                Log.d("성공", response.body().toString())
//                accessToken = response.body()?.id.toString()
//                refreshToken = response.body()?.password.toString()
//            }
//        })


        return view
    }

    private fun setFolderAdapter(List: ArrayList<ReadFoldersResponse>){
        // 폴더리스트 어댑터 연결
        binding.recyclerviewFolderList.layoutManager = GridLayoutManager(requireContext(), 2)
        Log.d("folderOfList 연결", List.toString())
        folderListAdapter = FolderListAdapter(List)
        binding.recyclerviewFolderList.adapter = folderListAdapter
//        binding.recyclerviewFolderList.setHasFixedSize(false)
    }

    fun loadData() {
        // 폴더 리스트 가져오기 api 연동
        api.readFolder("JWT eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6InN1bWluIiwiZW1haWwiOiJzdW1pbkBuYXZlci5jb20iLCJuaWNrbmFtZSI6InN1bWluIiwiaWF0IjoxNjg1NjE2NTAwLCJleHAiOjE2ODU2MjAxMDB9.JGnqSiSnkuSLHG6Pt5YZWiKvacpxsNv_2DpTaPmmdPw", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6InN1bWluIiwiZW1haWwiOiJzdW1pbkBuYXZlci5jb20iLCJuaWNrbmFtZSI6InN1bWluIiwiaWF0IjoxNjg1NjE2NTAwLCJleHAiOjE2ODYyMjEzMDB9.p0NJoSlu62xqrmSn865wbaZLDzTvirmX7gHxwzxPhFI")
            .enqueue(object: Callback<ReadFoldersList> {
                override fun onFailure(call: Call<ReadFoldersList>, t: Throwable) {
                    Log.d("실패", t.message.toString())
                }
                override fun onResponse(call: Call<ReadFoldersList>, response: Response<ReadFoldersList>) {
                    Log.d("성공", response.body().toString())
                    if (response.body() != null) {
                        response.body()?.let {
                            folderOfList = response.body()?.get()!!
                            Log.d("folderOfList 반환", folderOfList.toString())
//                            folderListAdapter.notifyDataSetChanged()
                            setFolderAdapter(folderOfList)
                        }
                    }
//                var a :ReadFoldersList = ReadFoldersList(arrayListOf(ReadFoldersResponse(1, 1, "name", "", false, 0)))
//                a.get()
                }
            })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //폴더 어댑터 연결
        binding.recyclerviewFolderList.layoutManager = GridLayoutManager(requireContext(), 2)
        Log.d("folderOfList 연결", folderOfList.toString())
        folderListAdapter = FolderListAdapter(folderOfList)
        binding.recyclerviewFolderList.adapter = folderListAdapter

        // 폴더 정렬 스피너 어댑터 연결
//        val sortFolderAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.item_sort_folder, android.R.layout.simple_spinner_dropdown_item)
//        binding.spinnerSortFolder.adapter = sortFolderAdapter
        val data: ArrayList<String> = arrayListOf()
        data.add("생성순")
        data.add("가나다순")
        sortFolderAdapter = SortFolderAdapter(requireContext(), data)
        binding.spinnerSortFolder.adapter = sortFolderAdapter




        // 폴더 클릭시 폴더 내부 페이지
        folderListAdapter.setOnItemclickListner(object: FolderListAdapter.OnItemClickListner{
            override fun onItemClick(view: View, position: Int) {
                parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, LinkInFolderFragment())
                    .commit()
            }
        })


        // 메뉴 버튼 클릭 시 FolderMenuBottomSheet 띄움
        binding.btnFolderMenu.setOnClickListener {
            // 폴더 관리 바텀 시트
            val bottomSheetFragment = FolderMenuBottomSheetFragment()
            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
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

    //폴더 추가 메소드
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun addFolder(name: String){
//        val item = FolderListItem(name, createdDate = LocalDateTime.now())
//        folderOfList.add(item)
//    }
//    override fun onResume() {
//        super.onResume()
////        loadData()
//        Log.d("onResume", "폴더 추가")
//    }
//    fun func1(){
////        Log.d("메소드 테스트", "")
////        loadData()
////    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}