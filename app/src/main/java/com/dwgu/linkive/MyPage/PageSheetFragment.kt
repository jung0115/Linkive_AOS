package com.dwgu.linkive.MyPage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dwgu.linkive.MyPage.MyPageRecycler.PageSheetItem
import com.dwgu.linkive.MyPage.PageSheetRecycler.PageSheetAdapter
import com.dwgu.linkive.MyPage.PageSheetRecycler.PageSheetContentItem
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.FragmentPageSheetBinding

class PageSheetFragment : Fragment() {

    // ViewBinding Setting
    lateinit var binding: FragmentPageSheetBinding

    private lateinit var pageSheetAdapter: PageSheetAdapter
    val pageSheetContentItemList = mutableListOf<PageSheetContentItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPageSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // recyclerView 세팅
        initRecycler()

        addLayoutItem(PageSheetContentItem(1))
        addLayoutItem(PageSheetContentItem(2))
        addLayoutItem(PageSheetContentItem(3))
        addLayoutItem(PageSheetContentItem(4))
        addLayoutItem(PageSheetContentItem(5))

        setOnClickListener()
    }

    private fun setOnClickListener() {
        // text 버튼
        binding.btnAddText.setOnClickListener {
            addLayoutItem(PageSheetContentItem(1))
        }
        // image 버튼
        binding.btnAddImage.setOnClickListener {
            addLayoutItem(PageSheetContentItem(2))
        }
        // link 버튼
        binding.btnAddLink.setOnClickListener {
            addLayoutItem(PageSheetContentItem(3))
        }
        // location 버튼
        binding.btnAddLocation.setOnClickListener {
            addLayoutItem(PageSheetContentItem(4))
        }
        // check 버튼
        binding.btnAddCheckbox.setOnClickListener {
            addLayoutItem(PageSheetContentItem(5))
        }
        // code 버튼
        binding.btnAddCode.setOnClickListener {
            addLayoutItem(PageSheetContentItem(6))
        }
    }

    private fun initRecycler() {
        pageSheetAdapter = PageSheetAdapter(requireContext())

        binding.recyclerviewCustomPagesheetContent.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerviewCustomPagesheetContent.adapter = pageSheetAdapter
        binding.recyclerviewCustomPagesheetContent.isNestedScrollingEnabled = false
    }

    private fun addLayoutItem(item: PageSheetContentItem) {
        pageSheetContentItemList.apply {
            add(item)
            Log.d("item List: ", pageSheetContentItemList[0].toString())
        }
        pageSheetAdapter.notifyDataSetChanged()
    }

}