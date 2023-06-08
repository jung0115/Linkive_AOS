package com.dwgu.linkive.Folder

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.dwgu.linkive.Api.ApiClient
import com.dwgu.linkive.Folder.FolderApi.FolderInterface
import com.dwgu.linkive.Folder.FolderApi.ReadFoldersList
import com.dwgu.linkive.Folder.FolderApi.ReadLinkInFolderRequest
import com.dwgu.linkive.Folder.FolderApi.ReadLinkInFolderResponse
import com.dwgu.linkive.Login.GloabalApplication
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.FragmentCheckFolderPasswordBinding
import com.dwgu.linkive.databinding.FragmentLinkInFolderBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckFolderPasswordFragment(private val folder: ReadFoldersList.ReadFoldersResponse) : DialogFragment() {

    private var _binding: FragmentCheckFolderPasswordBinding? = null
    private val binding get() = _binding!!

    // retrofit builder 선언
    private val retrofit = ApiClient.getInstance()
    private val api: FolderInterface = retrofit.create(FolderInterface::class.java)

    // 토큰 값
    private lateinit var accessToken: String
    private lateinit var refreshToken: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // dialog 테두리 둥글게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        accessToken = "JWT ${GloabalApplication.prefs.getString("accessToken", "")}"
        refreshToken = GloabalApplication.prefs.getString("refreshToken", "")

        _binding = FragmentCheckFolderPasswordBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 초기화할 때는 비밀번호 숨김
        binding.edittextFolderPassword.transformationMethod = PasswordTransformationMethod.getInstance()

        // 비밀번호 표시/숨김
        binding.checkEye.setOnCheckedChangeListener { view, isChecked ->
            if (isChecked){
                binding.edittextFolderPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            else {
                binding.edittextFolderPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
            binding.edittextFolderPassword.setSelection(binding.edittextFolderPassword.text.length)
        }


        // editText에 focus 되면 error icon 없앰
        binding.edittextFolderPassword.onFocusChangeListener = View.OnFocusChangeListener{ view, hasFocus ->
            if (hasFocus) {
                binding.icPasswordError.visibility = View.GONE
            }
        }

        //취소 버튼
        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        //확인 버튼
        binding.btnConfirm.setOnClickListener {
            val password: String = binding.edittextFolderPassword.text.toString()

            val request = ReadLinkInFolderRequest(folder.folderNum, password)
            // 폴더 번호와 입력된 비밀번호를 이용해서 폴더 안의 메모들을 가져옴
            // 성공하면 LinkInFolderBottomSheet show
            // 실패하면 실패 아이콘 띄움
            api.readLinkInFolder(accessToken, refreshToken, request).enqueue(object : Callback<ReadLinkInFolderResponse> {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(call: Call<ReadLinkInFolderResponse>, response: Response<ReadLinkInFolderResponse>
                ) {
                    Log.d("패스워드 성공", response.body().toString())
                    if (response.body() != null){
                        parentFragmentManager
                            .beginTransaction()
                            .add(R.id.nav_host_fragment, LinkInFolderFragment(folder, password))
                            .commit()

                        // dialog 닫기
                        dismiss()
                    }
                    else {
                        Log.d("패스워드 틀림", response.body().toString())
                        // 왠진 모르겠지만 api 함수 내에서 binding을 사용하려면 이렇게 해야 함
                        // 쓰레드 개념
                        activity?.runOnUiThread {
                            binding.icPasswordError.visibility = View.VISIBLE
                            // 포커스 해제
                            binding.edittextFolderPassword.clearFocus()
                        }
                    }
                }

                override fun onFailure(call: Call<ReadLinkInFolderResponse>, t: Throwable) {
                    Log.d("패스워드 실패", t.toString())

                }

            })

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}