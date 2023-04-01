package com.dwgu.linkive.MyPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.FragmentMyPageBinding
import kotlinx.android.synthetic.main.page_sheet_item.view.*

class MyPageFragment : Fragment() {

    // view binding
    lateinit var binding: FragmentMyPageBinding

    // recyclerview에 adapter와 data 연결
    private lateinit var basicPageSheetAdapter: BasicPageSheetAdapter
    private lateinit var customPageSheetAdapter: CustomPageSheetAdapter
    val basicPageSheetList = mutableListOf<PageSheetItem>()
    val customPageSheetList = mutableListOf<PageSheetItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyPageBinding.inflate(layoutInflater)

        basicPageSheetList.apply {
            add(PageSheetItem(1, "여행"))
            add(PageSheetItem(2, "공부"))
            add(PageSheetItem(3, "개발"))
            add(PageSheetItem(4, "일기"))
            add(PageSheetItem(5, "체크\n리스트"))
        }

        customPageSheetList.apply {
            add(PageSheetItem(1, "대구"))
            add(PageSheetItem(2, "부산"))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        basicPageSheetAdapter = BasicPageSheetAdapter(
            this.basicPageSheetList,
            sheetClick = {

            }
        )

        customPageSheetAdapter = CustomPageSheetAdapter(
            this.customPageSheetList,
            sheetClick = {

            }
        )

        binding.basicBtnRecycler.adapter = basicPageSheetAdapter
        binding.customBtnRecycler.adapter = customPageSheetAdapter
    }

}