package com.dwgu.linkive.Folder

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.FragmentAddFolderBottomSheetBinding
import com.dwgu.linkive.databinding.FragmentFolderMenuBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddFolderBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAddFolderBottomSheetBinding? = null
    private val binding get() = _binding!!

    val bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddFolderBottomSheetBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.rgroupColor.setOnCheckedChangeListener { group, i ->
//            when(i){
//
//            }
//        }

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

        // 포커스 해제될 때
//        binding.edittextFolderPassword.setOnFocusChangeListener { view, bFocus ->
//            if(!bFocus){
//                binding.viewPasswordCheck.visibility = View.VISIBLE
//            }
//        }

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
                var color: Int = R.color.cover_red

                when(binding.rgroupColor.checkedRadioButtonId){
                    R.id.rbtn_red -> color = R.color.cover_red
                    R.id.rbtn_orange -> color = R.color.cover_orange
                    R.id.rbtn_yellow -> color = R.color.cover_yellow
                    R.id.rbtn_green -> color = R.color.cover_green
                    R.id.rbtn_blue -> color = R.color.cover_blue
                    R.id.rbtn_navy -> color = R.color.cover_navy
                    R.id.rbtn_purple -> color = R.color.cover_purple
                    R.id.rbtn_gray -> color = R.color.cover_gray
                }

                bundle.putInt("folderCover", color)
                bundle.putString("folderName", name)
                bundle.putBoolean("btnConfirm", true)



                val menuBottomSheetFragment = FolderMenuBottomSheetFragment()
                menuBottomSheetFragment.arguments = bundle

                val folderFragment = FolderFragment()
                folderFragment.arguments = bundle
                parentFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, folderFragment)
                    .commit()
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