package com.dwgu.linkive.Folder

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import com.dwgu.linkive.Api.ApiClient
import com.dwgu.linkive.Folder.FolderApi.*
import com.dwgu.linkive.Folder.LinkInFolderAdapter.LinkInFolderAdapter
import com.dwgu.linkive.Home.HomeLinkListRecycler.LinkListAdapter
import com.dwgu.linkive.Home.HomeLinkListRecycler.LinkListItem
import com.dwgu.linkive.LinkView.LinkViewFragment
import com.dwgu.linkive.Login.GloabalApplication
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.FragmentFolderBinding
import com.dwgu.linkive.databinding.FragmentHomeBinding
import com.dwgu.linkive.databinding.FragmentLinkInFolderBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LinkInFolderFragment(private val folder: ReadFoldersList.ReadFoldersResponse, private val password: String? = null) : Fragment() {

    // 링크 리스트 recyclerview adapter
    private var linkListItems = mutableListOf<LinkListItem>()
    private lateinit var linkInFolderAdapter: LinkInFolderAdapter

    private var _binding: FragmentLinkInFolderBinding? = null
    private val binding get() = _binding!!

    //retrofit builder 선언
    private val retrofit = ApiClient.getInstance()
    private val api: FolderInterface = retrofit.create(FolderInterface::class.java)

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
        _binding = FragmentLinkInFolderBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // recyclerview 세팅
        initRecycler()

        // 화면 세팅
        initView()

        val request = ReadLinkInFolderRequest(password)

        // 폴더 속의 메모들 가져오기
        api.readLinkInFolder(folder.folderNum ,accessToken, refreshToken, request).enqueue(object :
            Callback<ReadLinkInFolderResponse> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<ReadLinkInFolderResponse>, response: Response<ReadLinkInFolderResponse>
            ) {
                // 링크 리스트 가져와서 리사이클러뷰랑 연결하고 리사이클러뷰 초기화
            }

            override fun onFailure(call: Call<ReadLinkInFolderResponse>, t: Throwable) {
                Log.d("파일 속 링크 가져오기 실패", t.toString())
            }

        })

        // 폴더 이름 수정 버튼 클릭 시
        binding.btnEditFolderName.setOnClickListener {
            // 편집 버튼 대신 완료 버튼
            binding.btnEditFolderName.visibility = View.GONE
            binding.btnDoneEditFolderName.visibility = View.VISIBLE

            // textView 없애고 editText 부름
            binding.textFolderName.visibility = View.GONE
            binding.edittextFolderName.visibility = View.VISIBLE

        }

        binding.btnDoneEditFolderName.setOnClickListener {

            val editName : String = binding.edittextFolderName.text.toString()

            // editText의 내용이 없으면 toast
            if (editName == ""){
                Toast.makeText(requireContext(), R.string.folder_name_hint, Toast.LENGTH_SHORT).show()
            }
            else {
                val request = EditFolderRequest(folder.folderNum, editName, password, folder.thumbnail)

                // 이름 편집 api
                api.editFolder(accessToken, refreshToken, request).enqueue(object : Callback<String>{
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        // 왠진 모르겠지만 api 함수 내에서 binding을 사용하려면 이렇게 해야 함
                        // 쓰레드 개념
                        activity?.runOnUiThread {
                            binding.textFolderName.text = editName
                        }
                        Log.d("이름 편집 성공", response.body().toString())
                    }
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.d("이름 편집 실패", t.message.toString())
                    }
                })


                // 완료 버튼 대신 편집 버튼
                binding.btnDoneEditFolderName.visibility = View.GONE
                binding.btnEditFolderName.visibility = View.VISIBLE

                // editText 없애고 TextView 부름
                binding.edittextFolderName.visibility = View.GONE
                binding.textFolderName.visibility = View.VISIBLE
            }

        }

        //메뉴 버튼 클릭 시
        binding.btnMenu.setOnClickListener {
            // 폴더 관리 바텀 시트
            val bottomSheetFragment = LinkInFolderMenuBottomSheetFragment(folder)
            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
        }
    }

    private fun initRecycler() {
        // 링크 리스트 recyclerview 세팅
        linkInFolderAdapter = LinkInFolderAdapter(
            requireContext(),
            onClickLinkItem = {
                openLinkViewPage()
            }
        )
        binding.recyclerviewLinkList.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerviewLinkList.adapter = linkInFolderAdapter
        binding.recyclerviewLinkList.isNestedScrollingEnabled = false // 스크롤을 매끄럽게 해줌
        linkInFolderAdapter.items = linkListItems
    }


    // 링크 세부 페이지 열기
    private fun openLinkViewPage() {
        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .add(R.id.nav_host_fragment, LinkViewFragment())
            .commit()
    }

    private fun initView() {
        binding.textFolderName.text = folder.name
        binding.textCountLink.text = folder.memoCount.toString()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}