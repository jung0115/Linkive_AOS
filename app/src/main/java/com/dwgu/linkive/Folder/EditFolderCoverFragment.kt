package com.dwgu.linkive.Folder

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.dwgu.linkive.Api.ApiClient
import com.dwgu.linkive.Folder.FolderApi.EditFolderRequest
import com.dwgu.linkive.Folder.FolderApi.FolderInterface
import com.dwgu.linkive.Folder.FolderApi.ReadFoldersList
import com.dwgu.linkive.Login.GloabalApplication
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.FragmentAddFolderBottomSheetBinding
import com.dwgu.linkive.databinding.FragmentEditFolderCoverBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EditFolderCoverFragment(private val folder: ReadFoldersList.ReadFoldersResponse, private val password: String?) : BottomSheetDialogFragment() {

    private var _binding: FragmentEditFolderCoverBinding? = null
    private val binding get() = _binding!!

    // 토큰 값
    private lateinit var accessToken: String
    private lateinit var refreshToken: String

    //retrofit builder 선언
    private val retrofit = ApiClient.getInstance()
    private val api: FolderInterface = retrofit.create(FolderInterface::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accessToken = "JWT ${GloabalApplication.prefs.getString("accessToken", "")}"
        refreshToken = GloabalApplication.prefs.getString("refreshToken", "")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditFolderCoverBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 링크 커버 중에서 선택하기 버튼 클릭 시
        binding.layoutSelectInLinkCover.setOnClickListener {
            val bottomSheetFragment = SelectFolderCoverFragment(folder)
            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
            dismiss()
        }

        // 취소 버튼
        binding.btnCancel.setOnClickListener {
//            val bottomSheetFragment = LinkInFolderMenuBottomSheetFragment(folder)
//            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
            dismiss()
        }
        // 확인 버튼
        binding.btnConfirm.setOnClickListener {
            var color: String = "red"

            when(binding.rgroupColor.checkedRadioButtonId){
                R.id.rbtn_red -> color = "bgcolor_red.png"
                R.id.rbtn_orange -> color = "bgcolor_orange.png"
                R.id.rbtn_yellow -> color = "bgcolor_yellow.png"
                R.id.rbtn_green -> color = "bgcolor_green.png"
                R.id.rbtn_blue -> color = "bgcolor_blue.png"
                R.id.rbtn_navy -> color = "bgcolor_navy.png"
                R.id.rbtn_purple -> color = "bgcolor_purple.png"
                R.id.rbtn_gray -> color = "bgcolor_gray.png"
            }


            val request = EditFolderRequest(folder.folderNum, folder.name, password, password, color)

            // 썸네일 변경 api 연동
            api.editFolder(accessToken, refreshToken, request).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.d("썸네일 변경 성공", response.body().toString())
                }
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("썸네일 변경 실패", t.message.toString())
                }
            })

            dismiss()
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme).apply {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.isDraggable = false
        }
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}