package com.dwgu.linkive.LinkView.LinkViewRecycler

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
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

// 링크 뷰 페이지 아이템 Recyclerview Adapter
class LinkViewAdapter (private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // ViewBinding Setting
    private lateinit var imageBinding: ItemLinkViewImageBinding       // 이미지
    private lateinit var textBinding: ItemLinkViewTextBinding         // 글
    private lateinit var placeBinding: ItemLinkViewPlaceBinding       // 주소(장소)
    private lateinit var linkBinding: ItemLinkViewLinkBinding         // 링크
    private lateinit var codeBinding: ItemLinkViewCodeBinding         // 코드
    private lateinit var checkboxBinding: ItemLinkViewCheckboxBinding // 할 일 (체크박스)

    var items = mutableListOf<LinkViewItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        // 이미지
        TYPE_IMAGE -> {
            val view = LayoutInflater.from(context).inflate(R.layout.item_link_view_image, parent, false)

            imageBinding = ItemLinkViewImageBinding.bind(view)

            ImageHolder(ItemLinkViewImageBinding.bind(view))
        }

        // 글
        TYPE_TEXT -> {
            val view = LayoutInflater.from(context).inflate(R.layout.item_link_view_text, parent, false)

            textBinding = ItemLinkViewTextBinding.bind(view)

            TextHolder(ItemLinkViewTextBinding.bind(view))
        }

        // 주소(장소)
        TYPE_PLACE -> {
            val view = LayoutInflater.from(context).inflate(R.layout.item_link_view_place, parent, false)

            placeBinding = ItemLinkViewPlaceBinding.bind(view)

            PlaceHolder(ItemLinkViewPlaceBinding.bind(view))
        }

        // 링크
        TYPE_LINK -> {
            val view = LayoutInflater.from(context).inflate(R.layout.item_link_view_link, parent, false)

            linkBinding = ItemLinkViewLinkBinding.bind(view)

            LinkHolder(ItemLinkViewLinkBinding.bind(view))
        }

        // 코드
        TYPE_CODE -> {
            val view = LayoutInflater.from(context).inflate(R.layout.item_link_view_code, parent, false)

            codeBinding = ItemLinkViewCodeBinding.bind(view)

            CodeHolder(ItemLinkViewCodeBinding.bind(view))
        }

        // 할 일 (체크리스트)
        TYPE_CHECKBOX -> {
            val view = LayoutInflater.from(context).inflate(R.layout.item_link_view_checkbox, parent, false)

            checkboxBinding = ItemLinkViewCheckboxBinding.bind(view)

            CheckboxHolder(ItemLinkViewCheckboxBinding.bind(view))
        }

        else -> {
            throw IllegalStateException("Not Found ViewHolder Type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            // 이미지
            is ImageHolder -> {
                holder.bind(items[position] as LinkViewImageItem)
            }
            // 글
            is TextHolder -> {
                holder.bind(items[position] as LinkViewTextItem)
            }
            // 주소(장소)
            is PlaceHolder -> {
                holder.bind(items[position] as LinkViewPlaceItem)
            }
            // 링크
            is LinkHolder -> {
                holder.bind(items[position] as LinkViewLinkItem)
            }
            // 코드
            is CodeHolder -> {
                holder.bind(items[position] as LinkViewCodeItem)
            }
            // 할 일(체크박스)
            is CheckboxHolder -> {
                holder.bind(items[position] as LinkViewCheckboxItem)
            }
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = when (items[position]) {
        // 이미지
        is LinkViewImageItem -> {
            TYPE_IMAGE
        }
        // 글
        is LinkViewTextItem -> {
            TYPE_TEXT
        }
        // 주소 (장소)
        is LinkViewPlaceItem -> {
            TYPE_PLACE
        }
        // 링크
        is LinkViewLinkItem -> {
            TYPE_LINK
        }
        // 코드
        is LinkViewCodeItem -> {
            TYPE_CODE
        }
        // 할 일 (체크박스)
        is LinkViewCheckboxItem -> {
            TYPE_CHECKBOX
        }

        else -> {
            throw IllegalStateException("Not Found ViewHolder Type")
        }
    }

    fun addItems(item: LinkViewItem) {
        this.items.add(item)
        this.notifyDataSetChanged()
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
    inner class ImageHolder(private val binding: ItemLinkViewImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LinkViewImageItem) {
            // 이미지가 비어있을 때
            if(item.linkViewImage == null) {
                // 이미지 나타나는 곳 가리고, 빈값 보여주기
                binding.imgLinkViewImage.visibility = View.GONE
                binding.linearlayoutLinkViewImageNull.visibility = View.VISIBLE
            }
            else {
                // 이미지 나타나는 곳 보여주고, 빈값 가리기
                binding.imgLinkViewImage.visibility = View.VISIBLE
                binding.linearlayoutLinkViewImageNull.visibility = View.GONE

                // 이미지 url로 이미지 로드
                CoroutineScope(Dispatchers.Main).launch {
                    val imageBitmap: Bitmap? = withContext(Dispatchers.IO) {
                        ImageLoader.loadImage(item.linkViewImage!!)
                    }
                    binding.imgLinkViewImage.setImageBitmap(imageBitmap) // 이미지
                }
            }
        }
    }

    // 글
    inner class TextHolder(private val binding: ItemLinkViewTextBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LinkViewTextItem) {
            // 글이 비어있을 때
            if(item.linkViewText == null) {
                // 글 나타나는 곳 가리고, 빈값 보여주기
                binding.textLinkViewText.visibility = View.GONE
                binding.textLinkViewTextNull.visibility = View.VISIBLE
            }
            else {
                // 글 나타나는 곳 보여주고, 빈값 가리기
                binding.textLinkViewText.visibility = View.VISIBLE
                binding.textLinkViewTextNull.visibility = View.GONE

                // 글 내용
                binding.textLinkViewText.text = item.linkViewText
            }
        }
    }

    // 주소(장소)
    inner class PlaceHolder(private val binding: ItemLinkViewPlaceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LinkViewPlaceItem) {
            // 주소가 비어있을 때
            if(item.linkViewPlace1 == null) {
                // 주소 나타나는 곳 가리고, 빈값 보여주기
                binding.linearlayoutLinkViewPlace.visibility = View.GONE
                binding.linearlayoutLinkViewPlaceNull.visibility = View.VISIBLE
            }
            else {
                // 주소 나타나는 곳 보여주고, 빈값 가리기
                binding.linearlayoutLinkViewPlace.visibility = View.VISIBLE
                binding.linearlayoutLinkViewPlaceNull.visibility = View.GONE

                // 도로명 주소
                binding.textLinkViewPlace1.text = item.linkViewPlace1
                // 지번 주소
                binding.textLinkViewPlace2.text = "지번: " + item.linkViewPlace2
            }
        }
    }

    // 링크
    inner class LinkHolder(private val binding: ItemLinkViewLinkBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LinkViewLinkItem) {
            // 링크가 비어있을 때
            if(item.linkViewLinkTitle == null) {
                // 링크 나타나는 곳 가리고, 빈값 보여주기
                binding.linearlayoutLinkViewLink.visibility = View.GONE
                binding.linearlayoutLinkViewLinkNull.visibility = View.VISIBLE
            }
            else {
                // 링크 나타나는 곳 보여주고, 빈값 가리기
                binding.linearlayoutLinkViewLink.visibility = View.VISIBLE
                binding.linearlayoutLinkViewLinkNull.visibility = View.GONE

                // 링크 제목
                binding.textLinkViewLinkTitle.text = item.linkViewLinkTitle
                // 링크 url
                binding.textLinkViewLinkUrl.text = item.linkViewLinkUrl
            }
        }
    }

    // 코드
    inner class CodeHolder(private val binding: ItemLinkViewCodeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LinkViewCodeItem) {
            // 코드 내용이 없는 경우
            if(item.linkViewCode == null) {
                // 코드 나타나는 곳 가리고, 빈값 보여주기
                binding.textLinkViewCode.visibility = View.GONE
                binding.textLinkViewCodeNull.visibility = View.VISIBLE
            }
            else {
                // 코드 나타나는 곳 보여주고, 빈값 가리기
                binding.textLinkViewCode.visibility = View.VISIBLE
                binding.textLinkViewCodeNull.visibility = View.GONE

                // 코드 내용
                binding.textLinkViewCode.text = item.linkViewCode
            }
        }
    }

    // 할 일(체크박스)
    inner class CheckboxHolder(private val binding: ItemLinkViewCheckboxBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LinkViewCheckboxItem) {
            // 할 일 내용이 없는 경우
            if(item.linkViewCheckboxText == null) {
                // 할 일 나타나는 곳 가리고, 빈값 보여주기
                binding.textLinkViewCheckbox.visibility = View.GONE
                binding.textLinkViewCheckboxNull.visibility = View.VISIBLE
            }
            else {
                // 할 일 나타나는 곳 보여주고, 빈값 가리기
                binding.textLinkViewCheckbox.visibility = View.VISIBLE
                binding.textLinkViewCheckboxNull.visibility = View.GONE

                // 할 일 내용
                binding.textLinkViewCheckbox.text = item.linkViewCheckboxText
            }

            // 체크된 상태인 경우
            if(item.linkViewCheckboxChecked) {
                // 체크 완료 아이콘
                binding.imgLinkViewCheckbox.setImageResource(R.drawable.ic_link_item_form_checkbox)
                // 내용을 회색으로
                binding.textLinkViewCheckbox.setTextColor(ContextCompat.getColor(context, R.color.dark_gray))
                // 내용에 취소선 넣어주기
                binding.textLinkViewCheckbox.paintFlags = binding.textLinkViewCheckbox.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            // 체크가 안 된 경우
            else {
                // 체크 안 됨 아이콘
                binding.imgLinkViewCheckbox.setImageResource(R.drawable.ic_link_item_form_checkbox_unchecked)
                // 내용을 검정색으로
                binding.textLinkViewCheckbox.setTextColor(ContextCompat.getColor(context, R.color.black))
                // 내용에 취소선 해제
                binding.textLinkViewCheckbox.paintFlags = binding.textLinkViewCheckbox.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            // 체크박스 클릭 시
            binding.imgLinkViewCheckbox.setOnClickListener(View.OnClickListener {
                // 체크된 상태인 경우 -> 체크 해제
                if(item.linkViewCheckboxChecked) {
                    // 체크 안 됨 아이콘
                    binding.imgLinkViewCheckbox.setImageResource(R.drawable.ic_link_item_form_checkbox_unchecked)
                    // 내용을 검정색으로
                    binding.textLinkViewCheckbox.setTextColor(ContextCompat.getColor(context, R.color.black))
                    // 내용에 취소선 해제
                    binding.textLinkViewCheckbox.paintFlags = binding.textLinkViewCheckbox.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
                // 체크가 안 된 경우 -> 체크 선택
                else {
                    // 체크 완료 아이콘
                    binding.imgLinkViewCheckbox.setImageResource(R.drawable.ic_link_item_form_checkbox)
                    // 내용을 회색으로
                    binding.textLinkViewCheckbox.setTextColor(ContextCompat.getColor(context, R.color.dark_gray))
                    // 내용에 취소선 넣어주기
                    binding.textLinkViewCheckbox.paintFlags = binding.textLinkViewCheckbox.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
                item.linkViewCheckboxChecked = !item.linkViewCheckboxChecked
            })
        }
    }
}