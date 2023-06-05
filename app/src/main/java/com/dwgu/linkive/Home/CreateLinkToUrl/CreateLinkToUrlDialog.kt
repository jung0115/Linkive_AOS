package com.dwgu.linkive.Home.CreateLinkToUrl

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.dwgu.linkive.Home.HomeLinkListRecycler.LinkListData
import com.dwgu.linkive.LinkMemoApi.CreateLinkMemo.CreateLinkMemoData
import com.dwgu.linkive.LinkMemoApi.CreateLinkMemo.FolderList
import com.dwgu.linkive.LinkMemoApi.CreateLinkMemo.LinkMemoContent
import com.dwgu.linkive.LinkMemoApi.CreateLinkMemo.apiCreateLinkMemo
import com.dwgu.linkive.LinkMemoApi.CreateLinkMemo.apiGetAllFolders
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.DialogCreateLinkToUrlBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// URL로 링크 추가 Dialog
class CreateLinkToUrlDialog(
    private val context: Context
    ) : DialogFragment() {

    // ViewBinding
    private lateinit var binding : DialogCreateLinkToUrlBinding

    // 메인 페이지 링크 리스트 refresh
    private lateinit var refreshHomeListener: RefreshHomeListener

    // 제목 글자수
    final val TITLE_LENGHT = 15

    // 서버에서 받아온 폴더 리스트
    private var folderListForApi: MutableList<FolderList>? = null

    // 폴더 선택 Spinner
    // 폴더 리스트
    private val selectFolderList = mutableListOf<String>()
    // Spinner 어댑터
    private var selectFolderAdapter: ArrayAdapter<String>? = null
    // 선택된 폴더
    private var selectedFolder: String? = null

    // db에서 가져온 폴더 리스트 데이터
    private var folderList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // viewBinding
        binding = DialogCreateLinkToUrlBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setCancelable(false) // 다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함

        // URL 입력창 기본 입력 자판이 영문 자판으로 나오게
        binding.edittextInputUrl.privateImeOptions = "defaultInputmode=english"

        // 폴더 선택 Spinner
        //Resources.getSystem().getString(R.string.default_select_folder)
        selectFolderList.add("폴더 선택") // 폴더 선택 - Default
        selectFolderAdapter = ArrayAdapter(context, R.layout.item_spinner_link_list_sort, selectFolderList)
        binding.spinnerSelectFolder.adapter = selectFolderAdapter

        // server에서 폴더 리스트 받아오기
        apiGetAllFolders(
            setFolders = {
                setFolderList(it)
            }
        )

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
                    linkData = GetInfoForUrl(linkUrl)

                    if(linkData != null && linkData!!.linkTitle == "null") {
                        linkData!!.linkUrl = linkUrl
                        linkData!!.linkTitle = linkUrl.substring(0 until TITLE_LENGHT) + "..."
                    }

                    if(linkData != null) {
                        if (linkData!!.linkTitle.length > TITLE_LENGHT) {
                            linkData!!.linkTitle = linkData!!.linkTitle.substring(0 until TITLE_LENGHT) + "..."
                        }

                        // 썸네일 이미지가 있는 경우, content에 썸네일 이미지 추가
                        var thumbnail = mutableListOf<String>()
                        if(linkData!!.thumbnailImage != null)
                            thumbnail = mutableListOf<String>("{ \"type\" : \"image\", \"value\" : \"" + linkData!!.thumbnailImage + "\" }")

                        // 폴더를 선택한 경우 폴더 번호 정보 추가
                        var selectFolderNum: Int? = null
                        if(selectedFolder != null)
                            selectFolderNum = searchFolderNum(selectedFolder!!)

                        // 링크 메모 생성
                        apiCreateLinkMemo(
                            CreateLinkMemoData(
                                link = linkUrl,
                                title = linkData!!.linkTitle,
                                content = LinkMemoContent(null, thumbnail),
                                folder_num = selectFolderNum
                            ),
                            refreshHomeListener
                        )


                    }
                }

                dismiss() // Dialog 닫기
            }
        }
    }

    // 폴더 리스트
    private fun setFolderList(folders: MutableList<FolderList>?) {
        folderListForApi = folders

        // 폴더가 있는 경우 Spinner 선택 리스트로 추가
        if(folders != null) {
            for(folder in folders){
                folderList.add(folder.name)
            }
        }
        selectFolderList.addAll(folderList)
        selectFolderAdapter!!.notifyDataSetChanged()
    }

    // 폴더명으로 폴더 번호 찾기
    private fun searchFolderNum(folderName: String): Int? {
        if(folderListForApi != null) {
            for (folder in folderListForApi!!) {
                if(folder.name == folderName)
                    return folder.folder_num
            }
        }

        return null
    }

    // 메인 페이지 링크 리스트 refresh
    interface RefreshHomeListener {
        fun refreshHomeListener()
    }

    fun setRefreshHomeListener(refreshHomeListener: RefreshHomeListener) {
        this.refreshHomeListener = refreshHomeListener
    }

}