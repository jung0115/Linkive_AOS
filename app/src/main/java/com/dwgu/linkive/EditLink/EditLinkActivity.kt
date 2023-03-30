package com.dwgu.linkive.EditLink

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dwgu.linkive.EditLink.EditLinkBottomSheet.NoticeNotSaveBottomFragment
import com.dwgu.linkive.EditLink.EditLinkRecyclerview.*
import com.dwgu.linkive.LinkView.LinkViewBottomSheet.MoveFolderBottomFragment
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.ActivityEditLinkBinding

class EditLinkActivity : AppCompatActivity() {

    // ViewBinding Setting
    lateinit var binding: ActivityEditLinkBinding

    // PageSheet 선택 Spinner
    // PageSheet 리스트
    private val pagesheetList = mutableListOf<String>()
    // Spinner 어댑터
    private var pagesheetAdapter: ArrayAdapter<String>? = null
    // 선택된 PageSheet
    private var selectPagesheet: String? = null

    // 링크 edit 페이지 아이템 Recyclerview
    private val editLinkItems = mutableListOf<EditLinkItem>()
    private lateinit var editLinkAdapter: EditLinkAdapter

    // 제목 글자수 입력 제한
    private val titleLimit = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding
        binding = ActivityEditLinkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // recyclerview 세팅
        initRecycler()

        // PageSheet 선택 안 한 상태로 편집 페이지 들어오면 '자유'로 선택해두기

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
        pagesheetAdapter = ArrayAdapter(this, R.layout.item_spinner_select_pagesheet, pagesheetList)
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

        // 제목 샘플 데이터
        binding.edittextEditLinkTitle.setText("제목 테스트")

        // 제목 글자수 제한
        binding.edittextEditLinkTitle.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 입력 변화 감지
                var titleLength = binding.edittextEditLinkTitle.text.length

                // 글자수가 제한을 넘어갈 경우
                if(titleLength > titleLimit) {
                    // 키보드 내리기
                    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(binding.edittextEditLinkTitle.windowToken, 0)

                    // 경고
                    Toast.makeText(this@EditLinkActivity, "제목은 최대 10글자까지 입력 가능합니다.", Toast.LENGTH_SHORT).show()

                    // 글자수 넘어간 내용은 지우기
                    val cutTitle = binding.edittextEditLinkTitle.text.substring(0 until titleLimit)
                    binding.edittextEditLinkTitle.setText(cutTitle)

                    // 커서 위치를 마지막으로
                    binding.edittextEditLinkTitle.setSelection(titleLimit)
                }
            }
        })

        // 샘플 데이터
        addEditLinkItem(EditLinkImageItem(null))
        addEditLinkItem(EditLinkImageItem("https:/img.youtube.com/vi/UYGud3qJeFI/default.jpg"))
        addEditLinkItem(EditLinkTextItem(null))
        addEditLinkItem(EditLinkTextItem("글 입력\n테스트\n테스트"))
        addEditLinkItem(EditLinkPlaceItem(null, null))
        addEditLinkItem(EditLinkPlaceItem("서울 송파구 올림픽로 240", "잠실동 40-1"))
        addEditLinkItem(EditLinkLinkItem(null, null))
        addEditLinkItem(EditLinkLinkItem("백준 - 토마토(7569)", "https://www.acmicpc.net/problem/7569"))
        addEditLinkItem(EditLinkCodeItem(null))
        addEditLinkItem(EditLinkCodeItem("System.out.print(“Hello, World!”);\n\n" +
                "while(i < 10) {\ni++;\n}\n\nSystem.out.print(“Hello, World!”);\nSystem.out.print(“Hello, World!”);"))
        addEditLinkItem(EditLinkCheckboxItem(null, false))
        addEditLinkItem(EditLinkCheckboxItem("할 일 입력 테스트", false))
        addEditLinkItem(EditLinkCheckboxItem("할 일 입력 테스트", true))

        // 저장 버튼 선택 시
        binding.btnSaveEditLinkPage.setOnClickListener {
            // 페이지 닫기
            finish()
        }
    }

    // 링크 Edit 페이지 아이템 recyclerview 세팅
    private fun initRecycler() {
        editLinkAdapter = EditLinkAdapter(this)
        binding.recyclerviewEditLink.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerviewEditLink.adapter = editLinkAdapter
        //binding.recyclerviewEditLink.isNestedScrollingEnabled = false // 스크롤을 매끄럽게 해줌
        editLinkAdapter.items = editLinkItems
    }

    // 링크 Edit 페이지 아이템 추가
    private fun addEditLinkItem(item: EditLinkItem) {
        editLinkItems.apply {
            add(item)
        }
        editLinkAdapter.notifyDataSetChanged()
    }

    private var doubleBackToExit = false
    // 이전 버튼 - 폰에 있는 이전 버튼
    override fun onBackPressed() {
        //super.onBackPressed()
        // 내용 저장 안 하고 벗어나면 저장 안 되는 거 안내하는 BottomSheet 열기
        val bottomSheet = NoticeNotSaveBottomFragment()
        bottomSheet.show(supportFragmentManager, bottomSheet.tag)
    }
    fun runDelayed(millis: Long, function: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed(function, millis)
    }
}