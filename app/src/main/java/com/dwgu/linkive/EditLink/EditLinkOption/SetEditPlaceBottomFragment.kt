package com.dwgu.linkive.EditLink.EditLinkOption

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.dwgu.linkive.EditLink.EditLinkRecyclerview.EditLinkPlaceItem
import com.dwgu.linkive.EditLink.EditPlaceRecyclerview.SelectPlaceAdapter
import com.dwgu.linkive.EditLink.EditPlaceRecyclerview.SelectPlaceItem
import com.dwgu.linkive.KakaoMapApi.apiGetKakaoAddress
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.FragmentSetEditPlaceBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// 장소 선택 BottomSheet
class SetEditPlaceBottomFragment : BottomSheetDialogFragment() {

    // ViewBinding
    private lateinit var binding : FragmentSetEditPlaceBottomBinding

    // 장소 선택 BottomSheet 장소 아이템 Recyclerview
    private var selectPlaceItems = mutableListOf<SelectPlaceItem>()
    private lateinit var selectPlaceAdapter: SelectPlaceAdapter

    // 리사이클러뷰에서의 position 값
    final val POSITION_IN_RECYCLERVIEW = "position_in_recyclerview"

    var position: String? = null

    // 장소 입력 event 전달하기 위한 listener
    private lateinit var setPlaceItemListener: EditLinkOptionListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            setPlaceItemListener = context as EditLinkOptionListener
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
        // viewBinding
        binding = FragmentSetEditPlaceBottomBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 리사이클러뷰에서의 position 값
        position = arguments?.getString(POSITION_IN_RECYCLERVIEW)

        // recyclerview 세팅
        initRecycler()

        // 취소 버튼
        binding.btnCancelToCreatePlace.setOnClickListener {
            dismiss() // BottomSheet 닫기
        }

        // 장소 검색 키워드 입력창에서 엔터
        binding.edittextInputPlace.setOnEditorActionListener{v, id, event ->
            if(id == EditorInfo.IME_ACTION_GO){
                // 키워드 검색으로 장소 목록 가져오기
                inputPlaceKeyword()
            }
            true
        }

        // 확인 버튼
        binding.btnConfirmToCreatePlace.setOnClickListener {
            // 아직 키워드 검색을 하지 않은 경우 or 선택할 장소 목록이 없는 경우
            if(selectPlaceItems.size == 0) {
                // 키워드 검색으로 장소 목록 가져오기
                inputPlaceKeyword()
            }

            // 장소를 선택한 경우
            else {
                setPlaceInfo()
            }
        }
    }

    // 장소 선택 아이템 recyclerview 세팅
    private fun initRecycler() {
        selectPlaceAdapter = SelectPlaceAdapter(requireContext())
        binding.recyclerviewSelectPlace.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerviewSelectPlace.adapter = selectPlaceAdapter
        binding.recyclerviewSelectPlace.isNestedScrollingEnabled = false // 스크롤을 매끄럽게 해줌
        selectPlaceAdapter.items = selectPlaceItems
    }

    // 장소 선택 아이템 추가
    private fun addSelectPlaceItem(item: SelectPlaceItem) {
        selectPlaceItems.apply {
            add(item)
        }
        selectPlaceAdapter.notifyDataSetChanged()
    }

    // 장소 검색 키워드 입력 시
    private fun inputPlaceKeyword() {
        val placeKeyword = binding.edittextInputPlace.text.toString()

        // 주소 선택 리스트 초기화
        selectPlaceItems = mutableListOf<SelectPlaceItem>()
        selectPlaceAdapter.items = selectPlaceItems

        // 장소 검색 키워드를 입력하지 않은 경우
        if(placeKeyword == null || placeKeyword.equals("")) {
            Toast.makeText(context, getString(R.string.search_place_keyword), Toast.LENGTH_SHORT).show()
        }
        // 장소 검색 키워드를 입력한 경우
        else {
            // 주소 선택 리스트 초기화
            //selectPlaceItems = mutableListOf<SelectPlaceItem>()

            // 주소 검색 api
            apiGetKakaoAddress(
                placeKeyword,
                addPlaceList = {
                    addSelectPlaceItem(it)
                }
            )
        }
    }

    // 선택한 장소 정보 링크 편집 페이지 아이템에 반영
    fun setPlaceInfo() {
        for(place in selectPlaceItems) {
            // 선택된 장소
            if(place.selectPlace) {
                // 링크 편집 페이지에 반영
                setPlaceItemListener.setPlaceItemListener(
                    EditLinkPlaceItem(
                        editLinkPlace1 = place.loadAddress,
                        editLinkPlace2 = place.randAddress,
                        position = position!!.toInt()
                    )
                )
                dismiss()
                break
            }
        }
    }
}