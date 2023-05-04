package com.dwgu.linkive.EditLink

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.dwgu.linkive.EditLink.DragToMoveItem.ItemTouchCallback
import com.dwgu.linkive.EditLink.EditLinkBottomSheet.*
import com.dwgu.linkive.EditLink.EditLinkOption.EditLinkOptionListener
import com.dwgu.linkive.EditLink.EditLinkRecyclerview.*
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.ActivityEditLinkBinding


class EditLinkActivity : AppCompatActivity(), EditLinkOptionListener {

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
    // recyclerview adapter
    private lateinit var editLinkAdapter: EditLinkAdapter
    // recyclerview 아이템 이동 콜백 변수 : 드래그 시 이동하는 거
    private lateinit var itemTouchHelper: ItemTouchHelper

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

        // PageSheet 선택 Spinner -------------------------------------------------------------------
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

        // 제목 샘플 데이터 ----------------------------------------------------------------------------
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

        // 샘플 데이터 --------------------------------------------------------------------------------------------
        addEditLinkItem(EditLinkImageItem(null, null))
        addEditLinkItem(EditLinkImageItem("https:/img.youtube.com/vi/UYGud3qJeFI/default.jpg", null))
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

        // 저장 버튼 선택 시 --------------------------------------------------------------------------------------
        binding.btnSaveEditLinkPage.setOnClickListener {
            // 페이지 닫기
            finish()
        }

        // 아이템 추가 --------------------------------------------------------------------------------------------
        // 글 아이템 추가
        binding.btnAddItemText.setOnClickListener {
            // 글 아이템 추가
            addEditLinkItem(EditLinkTextItem(null))

            // 최하단으로 스크롤 이동
            binding.recyclerviewEditLink.smoothScrollToPosition(editLinkItems.size)
        }
        // 이미지 아이템 추가
        binding.btnAddItemImage.setOnClickListener {
            // 이미지 아이템 추가
            addEditLinkItem(EditLinkImageItem(null, null))

            // 최하단으로 스크롤 이동
            binding.recyclerviewEditLink.smoothScrollToPosition(editLinkItems.size)
        }
        // 링크 아이템 추가
        binding.btnAddItemLink.setOnClickListener {
            // 링크 아이템 추가
            addEditLinkItem(EditLinkLinkItem(null, null))

            // 최하단으로 스크롤 이동
            binding.recyclerviewEditLink.smoothScrollToPosition(editLinkItems.size)
        }
        // 장소(위치) 아이템 추가
        binding.btnAddItemPlace.setOnClickListener {
            // 장소(위치) 아이템 추가
            addEditLinkItem(EditLinkPlaceItem(null, null))

            // 최하단으로 스크롤 이동
            binding.recyclerviewEditLink.smoothScrollToPosition(editLinkItems.size)
        }
        // 체크박스(할 일) 아이템 추가
        binding.btnAddItemCheckbox.setOnClickListener {
            // 할 일 아이템 추가
            addEditLinkItem(EditLinkCheckboxItem(null, false))

            // 최하단으로 스크롤 이동
            binding.recyclerviewEditLink.smoothScrollToPosition(editLinkItems.size)
        }
        // 코드 아이템 추가
        binding.btnAddItemCode.setOnClickListener {
            // 코드 아이템 추가
            addEditLinkItem(EditLinkCodeItem(null))

            // 최하단으로 스크롤 이동
            binding.recyclerviewEditLink.smoothScrollToPosition(editLinkItems.size)
        }
    }

    // 링크 Edit 페이지 아이템 recyclerview 세팅
    private fun initRecycler() {
        editLinkAdapter = EditLinkAdapter(
            this,
            onClickItemOption = {
                openItemOptionBottomSheet(it)
            },
            onClickSelectImage = {
                navigatePhotos(it)
            }
        )
        // 드래그 이동 adapter
        itemTouchHelper = ItemTouchHelper(ItemTouchCallback(editLinkAdapter))
        itemTouchHelper.attachToRecyclerView(binding.recyclerviewEditLink)

        binding.recyclerviewEditLink.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerviewEditLink.adapter = editLinkAdapter
        //binding.recyclerviewEditLink.isNestedScrollingEnabled = false // 스크롤을 매끄럽게 해줌
        editLinkAdapter.items = editLinkItems
    }

    // 링크 Edit 페이지 아이템 추가
    private fun addEditLinkItem(item: EditLinkItem) {
        editLinkItems.add(item)
        editLinkAdapter.notifyDataSetChanged()
    }

    // 아이템 옵션 BottomSheet 열기
    private fun openItemOptionBottomSheet(itemInfo: MutableList<String>) {
        // 이미지 아이템 옵션 BottomSheet
        if(itemInfo[0] == "image") {
            val bottomSheet = OptionEditImageBottomFragment()

            // recycleview에서의 position 값 전달
            val bundle = Bundle()
            bundle.putString("position_in_recyclerview", itemInfo[1])
            bottomSheet.arguments = bundle

            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }
        // 글 아이템 옵션 BottomSheet
        else if(itemInfo[0] == "text") {
            val bottomSheet = OptionEditTextBottomFragment()

            // recycleview에서의 position 값 전달
            val bundle = Bundle()
            bundle.putString("position_in_recyclerview", itemInfo[1])
            bottomSheet.arguments = bundle

            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }
        // 주소(장소) 아이템 옵션 BottomSheet
        else if(itemInfo[0] == "place") {
            val bottomSheet = OptionEditPlaceBottomFragment()

            // recycleview에서의 position 값 전달
            val bundle = Bundle()
            bundle.putString("position_in_recyclerview", itemInfo[1])
            bottomSheet.arguments = bundle

            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }
        // 링크 아이템 옵션 BottomSheet
        else if(itemInfo[0] == "link") {
            val bottomSheet = OptionEditLinkBottomFragment()

            // recycleview에서의 position 값 전달
            val bundle = Bundle()
            bundle.putString("position_in_recyclerview", itemInfo[1])
            bottomSheet.arguments = bundle

            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }
        // 코드 아이템 옵션 BottomSheet
        else if(itemInfo[0] == "code") {
            val bottomSheet = OptionEditCodeBottomFragment()

            // recycleview에서의 position 값 전달
            val bundle = Bundle()
            bundle.putString("position_in_recyclerview", itemInfo[1])
            bottomSheet.arguments = bundle

            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }
        // 할 일 아이템 옵션 BottomSheet
        else if(itemInfo[0] == "checkbox") {
            val bottomSheet = OptionEditCheckboxBottomFragment()

            // recycleview에서의 position 값 전달
            val bundle = Bundle()
            bundle.putString("position_in_recyclerview", itemInfo[1])
            bottomSheet.arguments = bundle

            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }
    }

    // 아이템 삭제
    override fun deleteItemListener(position: Int) {
        editLinkItems.removeAt(position)
        editLinkAdapter.notifyDataSetChanged()
    }

    // 갤러리에서 이미지 선택한 거 반영해주기
    override fun selectImageListener(position: Int, imageUri: Uri) {
        editLinkItems[position] = EditLinkImageItem(null, imageUri)
        editLinkAdapter.notifyDataSetChanged()
    }

    // 링크 편집 페이지 글, 코드, 링크, 할 일 내용 리셋
    override fun resetItemListener(position: Int, itemType: String) {
        // 글 리셋
        if(itemType == "text") {
            editLinkItems[position] = EditLinkTextItem(null)
            editLinkAdapter.notifyDataSetChanged()
        }
        // 코드 리셋
        else if(itemType == "code") {
            editLinkItems[position] = EditLinkCodeItem(null)
            editLinkAdapter.notifyDataSetChanged()
        }
        // 링크 리셋
        else if(itemType == "link") {
            editLinkItems[position] = EditLinkLinkItem(null, null)
            editLinkAdapter.notifyDataSetChanged()
        }
        // 할 일 리셋
        else if(itemType == "checkbox") {
            editLinkItems[position] = EditLinkCheckboxItem(null, false)
            editLinkAdapter.notifyDataSetChanged()
        }
    }

    // 이전 버튼 - 폰에 있는 이전 버튼
    override fun onBackPressed() {
        //super.onBackPressed()
        // 내용 저장 안 하고 벗어나면 저장 안 되는 거 안내하는 BottomSheet 열기
        val bottomSheet = NoticeNotSaveBottomFragment()
        bottomSheet.show(supportFragmentManager, bottomSheet.tag)
    }

    private var selectImagePosition: Int? = null
    // 갤러리 오픈
    private fun navigatePhotos(position: Int) {
        selectImagePosition = position

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 2000)
    }

    // 갤러리에서 호출한 액티비티 결과 반환
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            Log.d("msg", "gallery error")
            return
        }
        when (requestCode) {
            2000 -> {
                val selectedImageURI: Uri? = data?.data
                if (selectedImageURI != null) {
                    // 선택한 이미지 전달
                    selectImageListener(selectImagePosition!!.toInt(), selectedImageURI)

                } else {
                    Log.d("msg", "gallery error")
                }
            }
            else -> {
                Log.d("msg", "gallery error")
            }
        }
    }
}