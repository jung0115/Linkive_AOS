package com.dwgu.linkive.Folder

import android.app.Dialog
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dwgu.linkive.Api.ApiClient
import com.dwgu.linkive.Folder.FolderApi.EditFolderRequest
import com.dwgu.linkive.Folder.FolderApi.FolderInterface
import com.dwgu.linkive.Folder.FolderApi.ReadFoldersList
import com.dwgu.linkive.Login.GloabalApplication
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.FragmentEditFolderPasswordBinding
import com.dwgu.linkive.databinding.FragmentSelectFolderCoverBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EditFolderPasswordFragment(private val folder: ReadFoldersList.ReadFoldersResponse, private val password: String?) : BottomSheetDialogFragment() {

    private var _binding: FragmentEditFolderPasswordBinding? = null
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
        _binding = FragmentEditFolderPasswordBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 초기화할 때는 비밀번호 숨김
        // 현재 비밀번호
        binding.edittextFolderPasswordNow.transformationMethod = PasswordTransformationMethod.getInstance()
        // 새 비밀번호
        binding.edittextFolderPasswordNew.transformationMethod = PasswordTransformationMethod.getInstance()
        // 새 비밀번호 체크
        binding.edittextFolderPasswordCheck.transformationMethod = PasswordTransformationMethod.getInstance()

        // 비밀번호 표시/숨김
        // 현재 비밀번호
        binding.checkEyeNow.setOnCheckedChangeListener { view, isChecked ->
            if (isChecked){
                binding.edittextFolderPasswordNow.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            else {
                binding.edittextFolderPasswordNow.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
            binding.edittextFolderPasswordNow.setSelection(binding.edittextFolderPasswordNow.text.length)
        }
        // 새 비밀번호
        binding.checkEyeNew.setOnCheckedChangeListener { view, isChecked ->
            if (isChecked){
                binding.edittextFolderPasswordNew.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            else {
                binding.edittextFolderPasswordNew.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
            binding.edittextFolderPasswordNew.setSelection(binding.edittextFolderPasswordNew.text.length)
        }
        // 새 비밀번호 체크
        binding.checkEyeCheck.setOnCheckedChangeListener { view, isChecked ->
            if (isChecked){
                binding.edittextFolderPasswordCheck.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            else {
                binding.edittextFolderPasswordCheck.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
            binding.edittextFolderPasswordCheck.setSelection(binding.edittextFolderPasswordCheck.text.length)
        }




        // 취소 버튼
        binding.btnCancel.setOnClickListener {
//            val bottomSheetFragment = LinkInFolderMenuBottomSheetFragment(folder)
//            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
            dismiss()
        }

        // 확인 버튼
        binding.btnConfirm.setOnClickListener {

            // 비밀번호 변경 api 연동
            val nowPassword = binding.edittextFolderPasswordNow.text.toString()
            val newPassword = binding.edittextFolderPasswordNew.text.toString()

            val request = EditFolderRequest(folder.folderNum, folder.name, nowPassword, newPassword, folder.thumbnail)

            // 비밀번호 설정 api 연동
            api.editFolder(accessToken, refreshToken, request).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.d("비밀번호 설정 성공", response.body().toString())
                }
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("비밀번호 설정 실패", t.message.toString())
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