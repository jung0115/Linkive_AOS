package com.dwgu.linkive.Folder

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dwgu.linkive.Api.ApiClient
import com.dwgu.linkive.Folder.FolderApi.*
import com.dwgu.linkive.Login.GloabalApplication
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.FragmentAddFolderBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddFolderBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAddFolderBottomSheetBinding? = null
    private val binding get() = _binding!!


    // retrofit builder 선언
    private val retrofit = ApiClient.getInstance()
    private val api: FolderInterface = retrofit.create(FolderInterface::class.java)

    // 토큰 값
    private lateinit var accessToken: String
    private lateinit var refreshToken: String

    // 폴더리스트 interface
    private lateinit var setFolderListListener: SetFolderListListener

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
        _binding = FragmentAddFolderBottomSheetBinding.inflate(inflater, container, false)
        val view = binding.root

//        api.login(LoginRequest("sumin", "sumin!")).enqueue(object : Callback<LoginRequest> {
//            override fun onFailure(call: Call<LoginRequest>, t: Throwable) {
//                Log.d("실패", t.message.toString())
//            }
//
//            override fun onResponse(call: Call<LoginRequest>, response: retrofit2.Response<LoginRequest>) {
//                Log.d("성공1", response.body().toString())
//                accessToken = response.body()?.id.toString()
//                refreshToken = response.body()?.password.toString()
//            }
//        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 초기화할 때는 비밀번호 숨김
        binding.edittextFolderPassword.transformationMethod = PasswordTransformationMethod.getInstance()
        binding.edittextFolderPasswordCheck.transformationMethod = PasswordTransformationMethod.getInstance()

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
        binding.checkEyeCheck.setOnCheckedChangeListener{ view, isChecked ->
            if (isChecked){
                binding.edittextFolderPasswordCheck.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            else {
                binding.edittextFolderPasswordCheck.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
            binding.edittextFolderPasswordCheck.setSelection(binding.edittextFolderPasswordCheck.text.length)
        }


        // 비밀번호를 입력하면 확인창 뜸
        // 비밀번호 지우면 확인창 없어짐
        binding.edittextFolderPassword.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if ( binding.edittextFolderPassword.text.toString().isEmpty()){
                    binding.viewPasswordCheck.visibility = View.GONE
                }
                else{
                    binding.viewPasswordCheck.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(p0: Editable?) {}

        })


        // 취소 버튼
        binding.btnCancel.setOnClickListener {
            val bottomSheetFragment = FolderMenuBottomSheetFragment()
            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
            dismiss()
        }
        // 확인 버튼
        binding.btnConfirm.setOnClickListener {

            // 폴더명 필수 입력
            if (binding.edittextFolderName.text.isEmpty()){
                Toast.makeText(requireContext(), R.string.folder_name_hint, Toast.LENGTH_SHORT).show()
            }
            // 비밀번호, 비밀번호 입력창 동일 확인
            else if(binding.edittextFolderPassword.text.toString() != binding.edittextFolderPasswordCheck.text.toString()) {
                binding.edittextFolderPasswordCheck.hint = getString(R.string.folder_password_error)
                binding.edittextFolderPasswordCheck.text = null
                binding.edittextFolderPasswordCheck.setHintTextColor(resources.getColor(R.color.red))
            }
            else {

                val name = binding.edittextFolderName.text.toString()
                var color: String = "red"
                var password: String? = null

                if (binding.edittextFolderPassword.text.toString() != ""){
                    password = binding.edittextFolderPassword.text.toString()
                    Log.d("password text", binding.edittextFolderPassword.text.toString())
                }

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

                val addFolderRequest =  AddFolderRequest(name, password, color)
                Log.d("폴더 추가 password", password.toString())

                //폴더 추가 api 연동
                api.createFolder(accessToken, refreshToken, addFolderRequest).enqueue(object: Callback<AddFolderResponse> {
                    override fun onFailure(call: Call<AddFolderResponse>, t: Throwable) {
                        Log.d("실패", t.message.toString())
                    }

                    override fun onResponse( call: Call<AddFolderResponse>, response: Response<AddFolderResponse> ) {
                        Log.d("성공1", response.body().toString())
                        setFolderListListener.setFolderList()
                    }
                })
                dismiss()
            }
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