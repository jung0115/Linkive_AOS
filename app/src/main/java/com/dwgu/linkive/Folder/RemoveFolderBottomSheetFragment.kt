package com.dwgu.linkive.Folder

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dwgu.linkive.Api.ApiClient
import com.dwgu.linkive.Folder.FolderApi.AddFolderResponse
import com.dwgu.linkive.Folder.FolderApi.FolderInterface
import com.dwgu.linkive.Folder.FolderApi.RemoveFolderRequest
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.FragmentFolderBinding
import com.dwgu.linkive.databinding.FragmentFolderMenuBottomSheetBinding
import com.dwgu.linkive.databinding.FragmentRemoveFolderBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RemoveFolderBottomSheetFragment(private val folderNum: Int) : BottomSheetDialogFragment() {

    private var _binding: FragmentRemoveFolderBottomSheetBinding? = null
    private val binding get() = _binding!!

    // retrofit builder 선언
    private val retrofit = ApiClient.getInstance()
    private val api: FolderInterface = retrofit.create(FolderInterface::class.java)

    // 폴더리스트 interface
    private lateinit var setFolderListListener: SetFolderListListener

    fun setListener(listener: SetFolderListListener){
        setFolderListListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRemoveFolderBottomSheetBinding.inflate(inflater, container, false)
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


        // 취소 버튼
        binding.btnCancel.setOnClickListener {
            val bottomSheetFragment = FolderMenuBottomSheetFragment()

            // 삭제 모드에서 view 모드로 전환
            setFolderListListener.setFolderList()
            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
            dismiss()
        }
        // 확인 버튼
        binding.btnConfirm.setOnClickListener {

            val removeFolderRequest = RemoveFolderRequest(folderNum)

            api.removeFolder("JWT eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6InN1bWluIiwiZW1haWwiOiJzdW1pbkBuYXZlci5jb20iLCJuaWNrbmFtZSI6InN1bWluIiwiaWF0IjoxNjg1NjE2NTAwLCJleHAiOjE2ODU2MjAxMDB9.JGnqSiSnkuSLHG6Pt5YZWiKvacpxsNv_2DpTaPmmdPw", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6InN1bWluIiwiZW1haWwiOiJzdW1pbkBuYXZlci5jb20iLCJuaWNrbmFtZSI6InN1bWluIiwiaWF0IjoxNjg1NjE2NTAwLCJleHAiOjE2ODYyMjEzMDB9.p0NJoSlu62xqrmSn865wbaZLDzTvirmX7gHxwzxPhFI"
                , removeFolderRequest).enqueue(object:
                Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("폴더 삭제 실패", t.message.toString())
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.d("folder_num", folderNum.toString())
                    Log.d("폴더 삭제 성공", response.body().toString())
                    // 삭제 모드에서 view 모드로 전환
                    // 해당 폴더가 삭제된 상태로 List 초기화
                    setFolderListListener.setFolderList()
                }
            })
            dismiss()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}