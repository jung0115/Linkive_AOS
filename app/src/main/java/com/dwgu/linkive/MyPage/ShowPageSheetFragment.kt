package com.dwgu.linkive.MyPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.FragmentMyPageBinding
import com.dwgu.linkive.databinding.FragmentShowPageSheetBinding

class ShowPageSheetFragment : Fragment() {

    // view binding
    lateinit var binding: FragmentShowPageSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowPageSheetBinding.inflate(layoutInflater)
        return binding.root
    }
}