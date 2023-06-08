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
import com.dwgu.linkive.Folder.FolderApi.ReadFoldersList
import com.dwgu.linkive.Folder.FolderApi.RemoveFolderRequest
import com.dwgu.linkive.Login.GloabalApplication
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


class RemoveFolderBottomSheetFragment(private val folder: ReadFoldersList.ReadFoldersResponse, private val mode: String) : BottomSheetDialogFragment() {

    private var _binding: FragmentRemoveFolderBottomSheetBinding? = null
    private val binding get() = _binding!!

    // retrofit builder 선언
    private val retrofit = ApiClient.getInstance()
    private val api: FolderInterface = retrofit.create(FolderInterface::class.java)

    // 폴더리스트 interface
    private lateinit var setFolderListListener: SetFolderListListener


    // 토큰 값
    private lateinit var accessToken: String
    private lateinit var refreshToken: String

    fun setListener(listener: SetFolderListListener){
        setFolderListListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accessToken = "JWT ${GloabalApplication.prefs.getString("accessToken", "")}"
        refreshToken = GloabalApplication.prefs.getString("refreshToken", "")
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

        // 폴더명 세팅
        binding.textviewFolderName.text = folder.name



        // 취소 버튼
        binding.btnCancel.setOnClickListener {
            // out : 폴더 외부 메뉴에서 호출당함
            if (mode == "out"){
                Log.d("폴더 외부에서 취소 버튼 누르기", "시도")
                val bottomSheetFragment = FolderMenuBottomSheetFragment()
                // 삭제 모드에서 view 모드로 전환
                setFolderListListener.setFolderList()
                bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
                Log.d("폴더 외부에서 취소 버튼 누르기", "성공")
                dismiss()
            }
            // in : 폴더 내부 메뉴에서 호출당함
            else if (mode == "in"){
                Log.d("폴더 내부에서 취소 버튼 누르기", "시도")
                dismiss()
            }
        }
        // 확인 버튼
        binding.btnConfirm.setOnClickListener {

            val removeFolderRequest = RemoveFolderRequest(folder.folderNum)

            api.removeFolder(accessToken, refreshToken, removeFolderRequest).enqueue(object:
                Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("폴더 삭제 실패", t.message.toString())
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.d("folder_num", folder.folderNum.toString())
                    Log.d("폴더 삭제 성공", response.body().toString())
                    // 삭제 모드에서 view 모드로 전환
                    // 해당 폴더가 삭제된 상태로 List 초기화
                    if (mode == "out"){
                        setFolderListListener.setFolderList()
                    }

                }
            })

            if (mode == "in"){
                Log.d("폴더 내부에서 확인 버튼 누르기", "시도")
                parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, FolderFragment())
                    .commit()
                Log.d("폴더 내부에서 확인 버튼 누르기", "성공")
            }

            dismiss()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}