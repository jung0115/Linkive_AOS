package com.dwgu.linkive.EditLink.EditLinkRecyclerview

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dwgu.linkive.ImageLoader.ImageLoader
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// 링크 편집 페이지 아이템 Recyclerview Adapter
class EditLinkAdapter (
    private val context: Context,
    val onClickItemOption: (itemCategory: MutableList<String>) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // ViewBinding Setting
    private lateinit var imageBinding: ItemEditLinkImageBinding       // 이미지
    private lateinit var textBinding: ItemEditLinkTextBinding         // 글
    private lateinit var placeBinding: ItemEditLinkPlaceBinding       // 주소(장소)
    private lateinit var linkBinding: ItemEditLinkLinkBinding         // 링크
    private lateinit var codeBinding: ItemEditLinkCodeBinding         // 코드
    private lateinit var checkboxBinding: ItemEditLinkCheckboxBinding // 할 일 (체크박스)

    var items = mutableListOf<EditLinkItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        // 이미지
        TYPE_IMAGE -> {
            val view = LayoutInflater.from(context).inflate(R.layout.item_edit_link_image, parent, false)

            imageBinding = ItemEditLinkImageBinding.bind(view)

            ImageHolder(ItemEditLinkImageBinding.bind(view))
        }

        // 글
        TYPE_TEXT -> {
            val view = LayoutInflater.from(context).inflate(R.layout.item_edit_link_text, parent, false)

            textBinding = ItemEditLinkTextBinding.bind(view)

            TextHolder(ItemEditLinkTextBinding.bind(view))
        }

        // 주소(장소)
        TYPE_PLACE -> {
            val view = LayoutInflater.from(context).inflate(R.layout.item_edit_link_place, parent, false)

            placeBinding = ItemEditLinkPlaceBinding.bind(view)

            PlaceHolder(ItemEditLinkPlaceBinding.bind(view))
        }

        // 링크
        TYPE_LINK -> {
            val view = LayoutInflater.from(context).inflate(R.layout.item_edit_link_link, parent, false)

            linkBinding = ItemEditLinkLinkBinding.bind(view)

            LinkHolder(ItemEditLinkLinkBinding.bind(view))
        }

        // 코드
        TYPE_CODE -> {
            val view = LayoutInflater.from(context).inflate(R.layout.item_edit_link_code, parent, false)

            codeBinding = ItemEditLinkCodeBinding.bind(view)

            CodeHolder(ItemEditLinkCodeBinding.bind(view))
        }

        // 할 일 (체크리스트)
        TYPE_CHECKBOX -> {
            val view = LayoutInflater.from(context).inflate(R.layout.item_edit_link_checkbox, parent, false)

            checkboxBinding = ItemEditLinkCheckboxBinding.bind(view)

            CheckboxHolder(ItemEditLinkCheckboxBinding.bind(view))
        }

        else -> {
            throw IllegalStateException("Not Found ViewHolder Type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            // 이미지
            is ImageHolder -> {
                holder.bind(items[position] as EditLinkImageItem, position)
            }
            // 글
            is TextHolder -> {
                holder.bind(items[position] as EditLinkTextItem, position)
            }
            // 주소(장소)
            is PlaceHolder -> {
                holder.bind(items[position] as EditLinkPlaceItem, position)
            }
            // 링크
            is LinkHolder -> {
                holder.bind(items[position] as EditLinkLinkItem, position)
            }
            // 코드
            is CodeHolder -> {
                holder.bind(items[position] as EditLinkCodeItem, position)
            }
            // 할 일(체크박스)
            is CheckboxHolder -> {
                holder.bind(items[position] as EditLinkCheckboxItem, position)
            }
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = when (items[position]) {
        // 이미지
        is EditLinkImageItem -> {
            TYPE_IMAGE
        }
        // 글
        is EditLinkTextItem -> {
            TYPE_TEXT
        }
        // 주소 (장소)
        is EditLinkPlaceItem -> {
            TYPE_PLACE
        }
        // 링크
        is EditLinkLinkItem -> {
            TYPE_LINK
        }
        // 코드
        is EditLinkCodeItem -> {
            TYPE_CODE
        }
        // 할 일 (체크박스)
        is EditLinkCheckboxItem -> {
            TYPE_CHECKBOX
        }

        else -> {
            throw IllegalStateException("Not Found ViewHolder Type")
        }
    }

    companion object {
        private const val TYPE_IMAGE = 0    // 이미지
        private const val TYPE_TEXT = 1     // 글
        private const val TYPE_PLACE = 2    // 주소 (장소)
        private const val TYPE_LINK = 3     // 링크
        private const val TYPE_CODE = 4     // 코드
        private const val TYPE_CHECKBOX = 5 // 할 일 (체크박스)
    }

    // 이미지
    inner class ImageHolder(private val binding: ItemEditLinkImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: EditLinkImageItem, position: Int) {
            // 이미지가 입력된 경우 - 이전에 선택된 이미지
            if(item.editLinkImage != null) {
                // 이미지 url로 이미지 로드
                CoroutineScope(Dispatchers.Main).launch {
                    val imageBitmap: Bitmap? = withContext(Dispatchers.IO) {
                        ImageLoader.loadImage(item.editLinkImage!!)
                    }
                    binding.imgEditLinkImageView.setImageBitmap(imageBitmap) // 이미지
                }
                // Edit 모드 가리고, View 모드 보여주기
                binding.linearlayoutEditLinkImage.visibility = View.GONE
                binding.imgEditLinkImageView.visibility = View.VISIBLE
            }
            // 편집 페이지에서 이미지 선택 시
            else if(item.editLinkImageUri != null) {
                binding.imgEditLinkImageView.setImageURI(item.editLinkImageUri)

                // Edit 모드 가리고, View 모드 보여주기
                binding.linearlayoutEditLinkImage.visibility = View.GONE
                binding.imgEditLinkImageView.visibility = View.VISIBLE
            }
            // 이미지가 입력되지 않은 경우
            else {
                // Edit 모드 보여주고, View 모드 가리기
                binding.linearlayoutEditLinkImage.visibility = View.VISIBLE
                binding.imgEditLinkImageView.visibility = View.GONE
            }

            // 옵션 버튼 눌렀을 때
            binding.btnOptionEditLinkImage.setOnClickListener {
                // 이미지 아이템 옵션 BottomSheet 열기
                onClickItemOption(mutableListOf("image", position.toString()))
            }
        }
    }

    // 글
    inner class TextHolder(private val binding: ItemEditLinkTextBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: EditLinkTextItem, position: Int) {
            // 글 내용이 입력된 경우
            if(item.editLinkText != null) {
                // 글 내용 세팅
                binding.edittextEditLinkText.setText(item.editLinkText)
            }
            // 글 내용이 없는 경우
            else {
                binding.edittextEditLinkText.setText(null)
            }

            // 옵션 버튼 눌렀을 때
            binding.btnOptionEditLinkText.setOnClickListener {
                // 글 아이템 옵션 BottomSheet 열기
                onClickItemOption(mutableListOf("text", position.toString()))
            }
        }
    }

    // 주소(장소)
    inner class PlaceHolder(private val binding: ItemEditLinkPlaceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: EditLinkPlaceItem, position: Int) {
            // 주소가 입력된 경우
            if(item.editLinkPlace1 != null) {
                // 주소 입력
                // 도로명 주소
                binding.textEditLinkPlace1View.text = item.editLinkPlace1
                // 지번 주소
                binding.textEditLinkPlace1View.text = "지번: " + item.editLinkPlace2

                // Edit 모드 가리고, View 모드 보여주기
                binding.relativelayoutEditLinkPlace.visibility = View.GONE
                binding.linearlayoutEditLinkPlaceView.visibility = View.VISIBLE
            }
            // 주소가 입력되지 않은 경우
            else {
                // 도로명 주소
                binding.textEditLinkPlace1View.text = null
                // 지번 주소
                binding.textEditLinkPlace1View.text = null

                // Edit 모드 보여주고, View 모드 가리기
                binding.relativelayoutEditLinkPlace.visibility = View.VISIBLE
                binding.linearlayoutEditLinkPlaceView.visibility = View.GONE
            }

            // 옵션 버튼 선택 시
            binding.btnOptionEditLinkPlace.setOnClickListener {
                // 주소 아이템 옵션 BottomSheet 열기
                onClickItemOption(mutableListOf("place", position.toString()))
            }

            // 주소 입력 버튼 선택 시
        }
    }

    // 링크
    inner class LinkHolder(private val binding: ItemEditLinkLinkBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: EditLinkLinkItem, position: Int) {
            // 링크가 입력된 경우
            if(item.editLinkLinkTitle != null) {
                // 링크 제목
                binding.textEditLinkLinkTitle.text = item.editLinkLinkTitle
                // 링크 url
                binding.textEditLinkLinkUrl.text = item.editLinkLinkUrl

                // Edit 모드 가리고, View 모드 보여주기
                binding.edittextEditLinkLink.visibility = View.GONE
                binding.linearlayoutEditLinkLink.visibility = View.VISIBLE
            }
            // 링크가 입력되지 않은 경우
            else {
                // 링크 제목
                binding.textEditLinkLinkTitle.text = null
                // 링크 url
                binding.textEditLinkLinkUrl.text = null

                // Edit 모드 보여주고, View 모드 가리기
                binding.edittextEditLinkLink.visibility = View.VISIBLE
                binding.linearlayoutEditLinkLink.visibility = View.GONE
            }

            // 옵션 버튼 선택 시
            binding.btnOptionEditLinkLink.setOnClickListener {
                // 링크 아이템 옵션 BottomSheet 열기
                onClickItemOption(mutableListOf("link", position.toString()))
            }

            // url 입력 후 엔터
        }
    }

    // 코드
    inner class CodeHolder(private val binding: ItemEditLinkCodeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: EditLinkCodeItem, position: Int) {
            // 코드 내용이 입력된 경우
            if(item.editLinkCode != null) {
                // 코드 내용
                binding.edittextEditLinkCode.setText(item.editLinkCode)
            }
            // 코드 내용이 없는 경우
            else {
                // 코드 내용
                binding.edittextEditLinkCode.setText(null)
            }

            // 옵션 버튼 선택 시
            binding.btnOptionEditLinkCode.setOnClickListener {
                // 코드 아이템 옵션 BottomSheet 열기
                onClickItemOption(mutableListOf("code", position.toString()))
            }
        }
    }

    // 할 일(체크박스)
    inner class CheckboxHolder(private val binding: ItemEditLinkCheckboxBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: EditLinkCheckboxItem, position: Int) {
            // 할 일 내용이 입력된 경우
            if(item.editLinkCheckboxText != null) {
                // 할 일 내용
                binding.edittextEditLinkCheckbox.setText(item.editLinkCheckboxText)
            }
            // 할 일 내용이 없는 경우
            else {
                // 할 일 내용
                binding.edittextEditLinkCheckbox.setText(null)
            }

            // 체크된 상태인 경우
            if(item.editLinkCheckboxChecked) {
                // 체크 완료 아이콘
                binding.imgEditLinkCheckbox.setImageResource(R.drawable.ic_link_item_form_checkbox)
                // 내용을 회색으로
                binding.edittextEditLinkCheckbox.setTextColor(ContextCompat.getColor(context!!, R.color.dark_gray))
            }
            // 체크가 안 된 경우
            else {
                // 체크 안 됨 아이콘
                binding.imgEditLinkCheckbox.setImageResource(R.drawable.ic_link_item_form_checkbox_unchecked)
                // 내용을 검정색으로
                binding.edittextEditLinkCheckbox.setTextColor(ContextCompat.getColor(context, R.color.black))
            }

            // 체크박스 클릭 시
            binding.imgEditLinkCheckbox.setOnClickListener(View.OnClickListener {
                // 체크가 안 된 경우 -> 체크 선택
                if(item.editLinkCheckboxChecked) {
                    // 체크 안 됨 아이콘
                    binding.imgEditLinkCheckbox.setImageResource(R.drawable.ic_link_item_form_checkbox_unchecked)
                    // 내용을 검정색으로
                    binding.edittextEditLinkCheckbox.setTextColor(ContextCompat.getColor(context, R.color.black))
                }
                // 체크된 상태인 경우 -> 체크 해제
                else {
                    // 체크 완료 아이콘
                    binding.imgEditLinkCheckbox.setImageResource(R.drawable.ic_link_item_form_checkbox)
                    // 내용을 회색으로
                    binding.edittextEditLinkCheckbox.setTextColor(ContextCompat.getColor(context, R.color.dark_gray))
                }
                item.editLinkCheckboxChecked = !item.editLinkCheckboxChecked
            })

            // 옵션 버튼 선택
            binding.btnOptionEditLinkCheckbox.setOnClickListener {
                // 할 일 아이템 옵션 BottomSheet 열기
                onClickItemOption(mutableListOf("checkbox", position.toString()))
            }
        }
    }
}