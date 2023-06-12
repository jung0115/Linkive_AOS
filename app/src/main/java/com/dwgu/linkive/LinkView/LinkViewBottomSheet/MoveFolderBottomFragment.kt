package com.dwgu.linkive.LinkView.LinkViewBottomSheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dwgu.linkive.LinkMemoApi.CreateLinkMemo.FolderList
import com.dwgu.linkive.LinkMemoApi.CreateLinkMemo.apiGetAllFolders
import com.dwgu.linkive.LinkMemoApi.CreateLinkMemo.apiMoveFolder
import com.dwgu.linkive.LinkView.LinkViewMenuListener.LinkViewMenuListener
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

    // 서버에서 받아온 폴더 리스트
    private var folderListForApi: MutableList<FolderList>? = null

    final val NUM_OF_LINK_MEMO = "memo_num"
    final val NUM_OF_FOLDER = "folder_num"

    // 링크 메모 번호
    private var memoNum: Int? = null
    // 폴더 번호
    private var folderNum: Int? = null

    // 링크 세부 페이지 다시 접속하기 위한 listener
    private lateinit var reopenLinkViewListener: LinkViewMenuListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            reopenLinkViewListener = context as LinkViewMenuListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                context.toString()
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoveFolderBottomBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 링크 메모 번호
        memoNum = requireArguments().getInt(NUM_OF_LINK_MEMO)

        // 링크 폴더 번호
        folderNum = requireArguments().getInt(NUM_OF_FOLDER)

        // recyclerview 세팅
        initRecycler()

        // server에서 폴더 리스트 받아오기
        apiGetAllFolders(
            setFolders = {
                setFolderList(it)
            }
        )

        // 취소 버튼 선택 시
        binding.btnCancelMoveFolder.setOnClickListener {
            dismiss()
        }

        // 확인 버튼 선택 시
        binding.btnConfirmMoveFolder.setOnClickListener {
            // 선택된 폴더 번호
            val selectFolderNum = selectFolder()

            // 폴더의 변화가 있는 경우
            if(selectFolderNum != null && selectFolderNum != folderNum) {
                // 폴더 이동
                apiMoveFolder(memoNum!!, selectFolderNum, reopenLinkViewListener)
            }

            // Dialog 닫기
            dismiss()
        }
    }

    // 폴더 이동 아이템 recyclerview 세팅
    private fun initRecycler() {
        moveFolderAdapter = MoveFolderAdapter(requireContext())
        binding.recyclerviewMoveFolder.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerviewMoveFolder.adapter = moveFolderAdapter
        binding.recyclerviewMoveFolder.isNestedScrollingEnabled = false // 스크롤을 매끄럽게 해줌
        moveFolderAdapter.items = moveFolderItems
    }

    // 폴더 이동 아이템 추가
    private fun addMoveFolderItem(item: MoveFolderItem) {
        moveFolderItems.apply {
            add(item)
        }
        moveFolderAdapter.notifyDataSetChanged()
    }

    // 폴더 리스트
    private fun setFolderList(folders: MutableList<FolderList>?) {
        folderListForApi = folders

        // 폴더가 있는 경우 Spinner 선택 리스트로 추가
        if(folders != null) {
            for(folder in folders){
                var isSelected = false
                // 현재 폴더 표시
                if(folder.folder_num == folderNum)
                    isSelected = true
                addMoveFolderItem(MoveFolderItem(folder.folder_num, folder.name, isSelected))
            }
        }

        // 현재 폴더가 없을 경우 첫 번째 폴더로 선택
        if(folderNum == -1) {
            moveFolderItems[0].selectFolder = true
            moveFolderAdapter.notifyDataSetChanged()
        }
    }

    // 선택된 폴더
    fun selectFolder(): Int? {
        for(folder in moveFolderItems) {
            // 선택된 폴더의 번호를 return
            if(folder.selectFolder)
                return folder.folderNum
        }

        return null
    }
}