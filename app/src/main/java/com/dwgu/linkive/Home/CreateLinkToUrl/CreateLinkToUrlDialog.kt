package com.dwgu.linkive.Home.CreateLinkToUrl

import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dwgu.linkive.Home.HomeLinkListRecycler.LinkListData
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.DialogCreateLinkToUrlBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// URL로 링크 추가 Dialog
class CreateLinkToUrlDialog(context: Context) : Dialog(context) {

    // ViewBinding
    private lateinit var binding : DialogCreateLinkToUrlBinding

    // 제목 글자수
    final val TITLE_LENGHT = 15

    // 폴더 선택 Spinner
    // 폴더 리스트
    private val selectFolderList = mutableListOf<String>()
    // Spinner 어댑터
    private var selectFolderAdapter: ArrayAdapter<String>? = null
    // 선택된 폴더
    private var selectedFolder: String? = null

    // db에서 가져온 폴더 리스트 데이터
    private var folderList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // viewBinding
        binding = DialogCreateLinkToUrlBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setCancelable(false) // 다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함

        // URL 입력창 기본 입력 자판이 영문 자판으로 나오게
        binding.edittextInputUrl.privateImeOptions = "defaultInputmode=english"

        // 폴더 예시
        folderList.add("폴더1")
        folderList.add("폴더2")
        folderList.add("폴더3")

        // 폴더 선택 Spinner
        //Resources.getSystem().getString(R.string.default_select_folder)
        selectFolderList.add("폴더 선택") // 폴더 선택 - Defalut
        selectFolderList.addAll(folderList)
        selectFolderAdapter = ArrayAdapter(context, R.layout.item_spinner_link_list_sort, selectFolderList)
        binding.spinnerSelectFolder.adapter = selectFolderAdapter

        // 폴더 선택 Spinner 누르면 폴더 리스트 나타남
        binding.spinnerSelectFolder.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // 선택된 폴더
                selectedFolder = binding.spinnerSelectFolder.getSelectedItem().toString()

                // 폴더를 선택하지 않은 경우
                if(selectedFolder == "폴더 선택") selectedFolder = null
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        // 취소 버튼
        binding.btnCancelToCreateLink.setOnClickListener {
            dismiss() // Dialog 닫기
        }

        // 확인 버튼
        binding.btnConfirmToCreateLink.setOnClickListener {
            val linkUrl = binding.edittextInputUrl.text.toString()

            // url을 입력하지 않은 경우
            if(linkUrl == null || linkUrl.equals("")) {
                Toast.makeText(context, "URL을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
            // url을 입력한 경우
            else {
                Log.d(ContentValues.TAG, "URL로 링크 생성 값 확인 -------------------------------------------")
                Log.d(ContentValues.TAG, "URL: " + linkUrl + ", folder: " + selectedFolder.toString())

                // 링크 url로 페이지 정보 가져오기: 제목, 썸네일 이미지, 출처 플랫폼 등
                var linkData: LinkListData?
                GlobalScope.launch(Dispatchers.IO) {
                    linkData = GetInfoForUrl(linkUrl, selectedFolder)

                    if(linkData != null) {
                        // 제목 글자수가 TITLE_LENGHT 자를 넘어가는 경우
                        if (linkData!!.linkTitle.length > TITLE_LENGHT) {
                            linkData!!.linkTitle = linkData!!.linkTitle.substring(0 until TITLE_LENGHT) + "..."
                        }
                    }
                }

                dismiss() // Dialog 닫기
            }
        }
    }

}