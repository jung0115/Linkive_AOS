package com.dwgu.linkive.EditLink

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.FragmentEditLinkBinding

class EditLinkFragment : Fragment() {

    // ViewBinding Setting
    lateinit var binding: FragmentEditLinkBinding

    // PageSheet 선택 Spinner
    // PageSheet 리스트
    private val pagesheetList = mutableListOf<String>()
    // Spinner 어댑터
    private var pagesheetAdapter: ArrayAdapter<String>? = null
    // 선택된 PageSheet
    private var selectPagesheet: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditLinkBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // PageSheet 선택 Spinner
        // 기본 PageSheet
        pagesheetList.add(getString(R.string.pagesheet_free))
        pagesheetList.add(getString(R.string.pagesheet_trip))
        pagesheetList.add(getString(R.string.pagesheet_study))
        pagesheetList.add(getString(R.string.pagesheet_programming))
        pagesheetList.add(getString(R.string.pagesheet_diary))
        pagesheetList.add(getString(R.string.pagesheet_checklist))
        // Custom PageSheet - 샘플 데이터
        pagesheetList.add("페이지시트1")
        pagesheetList.add("페이지시트2")
        pagesheetAdapter = ArrayAdapter(requireContext(), R.layout.item_spinner_select_pagesheet, pagesheetList)
        binding.spinnerSelectPagesheet.adapter = pagesheetAdapter

        // PageSheet 선택 Spinner 누르면 PageSheet 선택 리스트 나타남
        binding.spinnerSelectPagesheet.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // 선택된 PageSheet
                selectPagesheet = binding.spinnerSelectPagesheet.getSelectedItem().toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }
}