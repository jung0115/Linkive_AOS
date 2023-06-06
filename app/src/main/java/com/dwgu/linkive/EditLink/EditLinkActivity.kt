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
import com.dwgu.linkive.EditLink.EditLinkOption.SetEditLinkBottomFragment
import com.dwgu.linkive.EditLink.EditLinkRecyclerview.*
import com.dwgu.linkive.EditLink.EditLinkOption.SetEditPlaceBottomFragment
import com.dwgu.linkive.LinkMemoApi.CreateLinkMemo.apiGetAllPageSheet
import com.dwgu.linkive.LinkMemoApi.CreateLinkMemo.apiGetEditLinkMemo
import com.dwgu.linkive.LinkMemoApi.DetailLinkMemo.LinkMemoEditBaseInfo
import com.dwgu.linkive.LinkMemoApi.GetAllPageSheet.GetPageSheetData
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.ActivityEditLinkBinding


class EditLinkActivity : AppCompatActivity(), EditLinkOptionListener {

    // ViewBinding Setting
    private var _binding: ActivityEditLinkBinding? = null
    private val binding get() = _binding!!

    // PageSheet 선택 Spinner
    // PageSheet 리스트
    private val pagesheetList = mutableListOf<String>()
    // Spinner 어댑터
    private var pagesheetAdapter: ArrayAdapter<String>? = null
    // 선택된 PageSheet
    private var selectPagesheet: String? = null

    // 링크 edit 페이지 아이템 Recyclerview
    private var editLinkItems: MutableList<EditLinkItem>? = null
    // recyclerview adapter
    private lateinit var editLinkAdapter: EditLinkAdapter
    // recyclerview 아이템 이동 콜백 변수 : 드래그 시 이동하는 거
    private lateinit var itemTouchHelper: ItemTouchHelper

    // server에서 받아온 PageSheet
    private var allPagesheet: MutableList<GetPageSheetData>? = null

    // 제목 글자수 입력 제한
    private val titleLimit = 15

    // 아이템 갯수 제한
    private val itemLimit = 50

    final val NUM_OF_LINK_MEMO = "memo_num"
    // 링크 메모 번호
    private var memoNum: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding
        _binding = ActivityEditLinkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 메모 번호
        memoNum = intent.getIntExtra(NUM_OF_LINK_MEMO, 0)

        // recyclerview 세팅
        initRecycler()

        // PageSheet 선택 안 한 상태로 편집 페이지 들어오면 '자유'로 선택해두기

        // PageSheet 선택 Spinner -------------------------------------------------------------------
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

        // 페이지시트 전체 조회
        apiGetAllPageSheet(
            setAllpageSheet = {
                setAllPageSheet(it)
            }
        )

        // 내용 가져와서 세팅
        apiGetEditLinkMemo(
            memoNum!!,
            setLinkEditInfo = {
                setLinkEditInformation(it)
            },
            addLinkEditItem = {
                addEditLinkItem(it)
            }
        )

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
                    Toast.makeText(this@EditLinkActivity, "제목은 최대 "+ titleLimit + "글자까지 입력 가능합니다.", Toast.LENGTH_SHORT).show()

                    // 글자수 넘어간 내용은 지우기
                    val cutTitle = binding.edittextEditLinkTitle.text.substring(0 until titleLimit)
                    binding.edittextEditLinkTitle.setText(cutTitle)

                    // 커서 위치를 마지막으로
                    binding.edittextEditLinkTitle.setSelection(titleLimit)
                }
            }
        })

        // 저장 버튼 선택 시 --------------------------------------------------------------------------------------
        binding.btnSaveEditLinkPage.setOnClickListener {
            Toast.makeText(this, getString(R.string.toast_edit_done), Toast.LENGTH_SHORT).show()
            // 페이지 닫기
            finish()
        }

        // 아이템 추가 --------------------------------------------------------------------------------------------
        // 글 아이템 추가
        binding.btnAddItemText.setOnClickListener {
            if(editLinkItems!!.size > itemLimit) {
                Toast.makeText(this, getString(R.string.limit_contents), Toast.LENGTH_SHORT).show()
            }
            else {
                // 글 아이템 추가
                addEditLinkItem(EditLinkTextItem(null, editLinkItems!!.size))

                // 최하단으로 스크롤 이동
                binding.recyclerviewEditLink.smoothScrollToPosition(editLinkItems!!.size)
            }
        }
        // 이미지 아이템 추가
        binding.btnAddItemImage.setOnClickListener {
            if(editLinkItems!!.size > itemLimit) {
                Toast.makeText(this, getString(R.string.limit_contents), Toast.LENGTH_SHORT).show()
            }
            else {
                // 이미지 아이템 추가
                addEditLinkItem(EditLinkImageItem(null, null, editLinkItems!!.size))

                // 최하단으로 스크롤 이동
                binding.recyclerviewEditLink.smoothScrollToPosition(editLinkItems!!.size)
            }
        }
        // 링크 아이템 추가
        binding.btnAddItemLink.setOnClickListener {
            if(editLinkItems!!.size > itemLimit) {
                Toast.makeText(this, getString(R.string.limit_contents), Toast.LENGTH_SHORT).show()
            }
            else {
                // 링크 아이템 추가
                addEditLinkItem(EditLinkLinkItem(null, null, editLinkItems!!.size))

                // 최하단으로 스크롤 이동
                binding.recyclerviewEditLink.smoothScrollToPosition(editLinkItems!!.size)
            }
        }
        // 장소(위치) 아이템 추가
        binding.btnAddItemPlace.setOnClickListener {
            if(editLinkItems!!.size > itemLimit) {
                Toast.makeText(this, getString(R.string.limit_contents), Toast.LENGTH_SHORT).show()
            }
            else {
                // 장소(위치) 아이템 추가
                addEditLinkItem(EditLinkPlaceItem(null, null, editLinkItems!!.size))

                // 최하단으로 스크롤 이동
                binding.recyclerviewEditLink.smoothScrollToPosition(editLinkItems!!.size)
            }
        }
        // 체크박스(할 일) 아이템 추가
        binding.btnAddItemCheckbox.setOnClickListener {
            if(editLinkItems!!.size > itemLimit) {
                Toast.makeText(this, getString(R.string.limit_contents), Toast.LENGTH_SHORT).show()
            }
            else {
                // 할 일 아이템 추가
                addEditLinkItem(EditLinkCheckboxItem(null, false, editLinkItems!!.size))

                // 최하단으로 스크롤 이동
                binding.recyclerviewEditLink.smoothScrollToPosition(editLinkItems!!.size)
            }
        }
        // 코드 아이템 추가
        binding.btnAddItemCode.setOnClickListener {
            if(editLinkItems!!.size > itemLimit) {
                Toast.makeText(this, getString(R.string.limit_contents), Toast.LENGTH_SHORT).show()
            }
            else {
                // 코드 아이템 추가
                addEditLinkItem(EditLinkCodeItem(null, editLinkItems!!.size))

                // 최하단으로 스크롤 이동
                binding.recyclerviewEditLink.smoothScrollToPosition(editLinkItems!!.size)
            }
        }
    }

    // 페이지 정보 세팅 - 제목, 폴더, 출처 플랫폼, 선택된 PageSheet
    private fun setLinkEditInformation(baseInfo: LinkMemoEditBaseInfo) {
        // 제목
        binding.edittextEditLinkTitle.setText(baseInfo.title)

        // 출처 플랫폼
        // 출처 플랫폼이 존재하는 경우
        if(baseInfo.source != null) {
            val iconResourceName = "ic_link_list_item_source_" + baseInfo.source
            val iconResourceId = resources.getIdentifier(iconResourceName, "drawable", packageName)
            binding.imgEditLinkSource.setImageResource(iconResourceId)
        }

        // 페이지 시트
        for(i in 0 until allPagesheet!!.size) {
            // 현재 페이지에 적용된 페이지 시트
            if(allPagesheet!![i].pagesheet_num == baseInfo.pagesheetNum) {
                selectPagesheet = allPagesheet!![i].name
                binding.spinnerSelectPagesheet.setSelection(i)
            }
        }
    }

    // 링크 Edit 페이지 아이템 recyclerview 세팅
    private fun initRecycler() {
        editLinkItems = mutableListOf<EditLinkItem>()
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
        editLinkAdapter.items = editLinkItems!!
    }

    // 링크 Edit 페이지 아이템 추가
    private fun addEditLinkItem(item: EditLinkItem) {
        editLinkItems!!.add(item)
        editLinkAdapter.notifyDataSetChanged()
    }

    // 페이지시트 전체 조회 후 Spinner 적용
    private fun setAllPageSheet(pagesheets: MutableList<GetPageSheetData>) {
        allPagesheet = pagesheets

        // PageSheet
        pagesheetList.add(getString(R.string.pagesheet_free))

        for(pageSheet in pagesheets) {
            pagesheetList.add(pageSheet.name)
        }

        pagesheetAdapter!!.notifyDataSetChanged()
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
        // 주소(장소) 검색 BottomSheet
        else if(itemInfo[0] == "place_search") {
            val bottomSheet = SetEditPlaceBottomFragment()

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
        // 링크 url 입력 BottomSheet
        else if(itemInfo[0] == "link_url") {
            // 링크 url 입력 BottomSheet 열기
            val bottomSheet = SetEditLinkBottomFragment()

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
        editLinkItems!!.removeAt(position)
        editLinkAdapter.notifyDataSetChanged()
    }

    // 갤러리에서 이미지 선택한 거 반영해주기
    override fun selectImageListener(position: Int, imageUri: Uri) {
        editLinkItems!![position] = EditLinkImageItem(null, imageUri, position)
        editLinkAdapter.notifyDataSetChanged()
    }

    // 링크 편집 페이지 글, 코드, 링크, 할 일 내용 리셋
    override fun resetItemListener(position: Int, itemType: String) {
        // 글 리셋
        if(itemType == "text") {
            editLinkItems!![position] = EditLinkTextItem(null, position)
            editLinkAdapter.notifyDataSetChanged()
        }
        // 코드 리셋
        else if(itemType == "code") {
            editLinkItems!![position] = EditLinkCodeItem(null, position)
            editLinkAdapter.notifyDataSetChanged()
        }
        // 링크 리셋
        else if(itemType == "link") {
            editLinkItems!![position] = EditLinkLinkItem(null, null, position)
            editLinkAdapter.notifyDataSetChanged()
        }
        // 할 일 리셋
        else if(itemType == "checkbox") {
            editLinkItems!![position] = EditLinkCheckboxItem(null, false, position)
            editLinkAdapter.notifyDataSetChanged()
        }
    }

    // 링크 편집 페이지 > 링크 아이템 내용 적용 (url에서 가져온 내용)
    override fun setLinkItemListener(position: Int, linkTile: String?, linkUrl: String?) {
        editLinkItems!![position] = EditLinkLinkItem(linkTile, linkUrl, position)
        editLinkAdapter.notifyDataSetChanged()
    }

    // 링크 편집 페이지 > 장소 아이템 내용 적용 (카카오 api에서 가져온 내용)
    override fun setPlaceItemListener(placeItem: EditLinkPlaceItem) {
        editLinkItems!![placeItem.position] = placeItem
        editLinkAdapter.notifyDataSetChanged()
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}