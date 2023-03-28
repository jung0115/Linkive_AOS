package com.dwgu.linkive.LinkView.LinkViewBottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dwgu.linkive.LinkView.LinkViewRecycler.LinkViewAdapter
import com.dwgu.linkive.LinkView.LinkViewRecycler.LinkViewItem
import com.dwgu.linkive.LinkView.MoveFolderRecycler.MoveFolderAdapter
import com.dwgu.linkive.LinkView.MoveFolderRecycler.MoveFolderItem
import com.dwgu.linkive.databinding.FragmentMoveFolderBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// 폴더 이동 BottomSheet
class MoveFolderBottomFragment : BottomSheetDialogFragment() {

    // ViewBinding Setting
    lateinit var binding: FragmentMoveFolderBottomBinding

    // 폴더 이동 BottomSheet 폴더명 아이템 Recyclerview
    private val moveFolderItems = mutableListOf<MoveFolderItem>()
    private lateinit var moveFolderAdapter: MoveFolderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoveFolderBottomBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // recyclerview 세팅
        initRecycler()

        // 샘플 데이터
        addMoveFolderItem(MoveFolderItem("폴더1", true)) // 첫번째 값 선택하도록
        addMoveFolderItem(MoveFolderItem("폴더2"))
        addMoveFolderItem(MoveFolderItem("폴더3"))
    }

    // 폴더 이동 아이템 recyclerview 세팅
    private fun initRecycler() {
        moveFolderAdapter = MoveFolderAdapter(requireContext())
        binding.recyclerviewMoveFolder.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerviewMoveFolder.adapter = moveFolderAdapter
        //binding.recyclerviewMoveFolder.isNestedScrollingEnabled = false // 스크롤을 매끄럽게 해줌
        moveFolderAdapter.items = moveFolderItems
    }

    // 폴더 이동 아이템 추가
    private fun addMoveFolderItem(item: MoveFolderItem) {
        moveFolderItems.apply {
            add(item)
        }
        moveFolderAdapter.notifyDataSetChanged()
    }
}