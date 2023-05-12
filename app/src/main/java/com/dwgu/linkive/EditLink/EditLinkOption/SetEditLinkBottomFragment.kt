package com.dwgu.linkive.EditLink.EditLinkOption

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.dwgu.linkive.Home.CreateLinkToUrl.GetInfoForUrl
import com.dwgu.linkive.Home.HomeLinkListRecycler.LinkListData
import com.dwgu.linkive.databinding.FragmentSetEditLinkBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// 링크 편집 > 링크 item > URL로 링크 추가 Dialog
class SetEditLinkBottomFragment : BottomSheetDialogFragment() {

    // ViewBinding
    private lateinit var binding : FragmentSetEditLinkBottomBinding

    // 리사이클러뷰에서의 position 값
    final val POSITION_IN_RECYCLERVIEW = "position_in_recyclerview"

    var position: String? = null

    // url에서 가져온 데이터
    private var linkData: LinkListData? = null

    // 링크 초기화 event 전달하기 위한 listener
    private lateinit var setLinkItemListener: EditLinkOptionListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            setLinkItemListener = context as EditLinkOptionListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                context.toString()
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // viewBinding
        binding = FragmentSetEditLinkBottomBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 리사이클러뷰에서의 position 값
        position = arguments?.getString(POSITION_IN_RECYCLERVIEW)

        // URL 입력창 기본 입력 자판이 영문 자판으로 나오게
        binding.edittextInputUrl.privateImeOptions = "defaultInputmode=english"

        // 취소 버튼
        binding.btnCancelToCreateLink.setOnClickListener {
            dismiss() // BottomSheet 닫기
        }

        // url 입력창에서 엔터
        binding.edittextInputUrl.setOnEditorActionListener{v, id, event ->
            if(id == EditorInfo.IME_ACTION_GO){
                val linkUrl = binding.edittextInputUrl.text.toString()

                // url을 입력하지 않은 경우
                if(linkUrl == null || linkUrl.equals("")) {
                    Toast.makeText(context, "URL을 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
                // url을 입력한 경우
                else {
                    // 링크 url로 페이지 정보 가져오기: 제목, 썸네일 이미지, 출처 플랫폼 등
                    var linkData: LinkListData? = null
                    GlobalScope.launch(Dispatchers.IO) {
                        linkData = GetInfoForUrl(linkUrl, null)

                        if(linkData != null && linkData!!.linkTitle == "null") {
                            linkData!!.linkUrl = linkUrl
                            linkData!!.linkTitle = linkUrl.substring(0 until 20) + "..."
                        }

                        // MainThread가 아닌 다른 Thread에서는 UI 변경이 불가능하기 때문에 handler를 이용해서 변경
                        val handler: Handler = object: Handler(Looper.getMainLooper()){
                            override fun handleMessage(msg: Message) {
                                // recyclerview에 값 전달해서 적용
                                setLinkItemListener.setLinkItemListener(position!!.toInt(), linkData!!.linkTitle, linkData!!.linkUrl)
                            }
                        }
                        handler.obtainMessage().sendToTarget()
                    }

                    dismiss() // BottomSheet 닫기
                }
            }
            true
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
                // 링크 url로 페이지 정보 가져오기: 제목, 썸네일 이미지, 출처 플랫폼 등
                GlobalScope.launch(Dispatchers.IO) {
                    linkData = GetInfoForUrl(linkUrl, null)

                    if(linkData != null && linkData!!.linkTitle == "null") {
                        linkData!!.linkUrl = linkUrl
                        linkData!!.linkTitle = linkUrl.substring(0 until 20) + "..."
                    }

                    // MainThread가 아닌 다른 Thread에서는 UI 변경이 불가능하기 때문에 handler를 이용해서 변경
                    val handler: Handler = object: Handler(Looper.getMainLooper()){
                        override fun handleMessage(msg: Message) {
                            // recyclerview에 값 전달해서 적용
                            setLinkItemListener.setLinkItemListener(position!!.toInt(), linkData!!.linkTitle, linkData!!.linkUrl)
                        }
                    }
                    handler.obtainMessage().sendToTarget()
                }
                dismiss() // BottomSheet 닫기
            }
        }
    }
}