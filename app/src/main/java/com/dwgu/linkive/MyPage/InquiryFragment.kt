package com.dwgu.linkive.MyPage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.FragmentInquiryBinding

class InquiryFragment : Fragment() {

    private lateinit var binding: FragmentInquiryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInquiryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.btnCloseInquiry.setOnClickListener {
            Log.d("msg", "문의 화면 종료")
            view?.findNavController()?.navigate(R.id.action_inquiryFragment_to_menu_mypage)
        }
    }
}