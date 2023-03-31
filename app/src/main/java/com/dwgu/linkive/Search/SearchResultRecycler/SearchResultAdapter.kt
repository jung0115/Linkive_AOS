package com.dwgu.linkive.Search.SearchResultRecycler

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dwgu.linkive.Home.HomeLinkListRecycler.LinkItemFormAdapter
import com.dwgu.linkive.Home.HomeLinkListRecycler.LinkListItem
import com.dwgu.linkive.ImageLoader.ImageLoader
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.ItemLinkOfListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// 검색 결과 RecyclerView Adapter
class SearchResultAdapter (
    private val context: Context,
    private val searchWord: String, // 검색어
    private val searchTab: String,  // 검색 Tab 이름: 전체(all), 제목(title), 본문(contents), 폴더명(folder), 장소(place)
    val onClickLinkItem: () -> Unit
) :
    RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder>() {

    var items = mutableListOf<LinkListItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) :
            SearchResultViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_link_of_list, parent, false)

        return SearchResultViewHolder(ItemLinkOfListBinding.bind(view))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bind(items[position])

        // 링크 아이템 클릭 시
        holder.itemView.setOnClickListener {
            onClickLinkItem()
        }
    }

    inner class SearchResultViewHolder(private val binding: ItemLinkOfListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LinkListItem) {

            // 제목
            binding.textviewLinkItemTitle.text = item.linkTitle
            // 검색 결과 탭 - 전체 또는 제목일 때
            if(searchTab == "all" || searchTab == "title") {
                // 제목에서 검색어 위치 찾아서 그 부분 색상 변경하기
                var index = item.linkTitle.indexOf(searchWord, 0)
                while(index != -1) {
                    // 제목에서 검색어 부분만 색상 변경하기 위한 작업
                    // 제목 데이터 SpannableStringBuilder 타입으로 변환
                    val builder = SpannableStringBuilder(item.linkTitle)
                    // index부터 검색어 끝부분까지 색상 적용
                    val highlightColor = ForegroundColorSpan(context.resources.getColor(R.color.main_color))
                    builder.setSpan(highlightColor, index, index + searchWord.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    // 제목 textview에 적용
                    binding.textviewLinkItemTitle.text = builder

                    // 제목에서 검색어 위치 탐색
                    index = item.linkTitle.indexOf(searchWord, index + 1)
                }
            }

            // 폴더가 존재할 경우
            if(item.folderName != null) {
                binding.imgLinkItemFolder.setImageResource(R.drawable.ic_folder_exist) // 폴더 존재 아이콘
                binding.textviewLinkItemFolder.text = item.folderName                  // 폴더명

                // 검색 결과 탭 - 전체 또는 폴더일 때
                if(searchTab == "all" || searchTab == "folder") {
                    // 폴더에서 검색어 위치 찾아서 그 부분 색상 변경하기
                    var index = item.folderName.indexOf(searchWord, 0)
                    while(index != -1) {
                        // 폴더에서 검색어 부분만 색상 변경하기 위한 작업
                        // 폴더 데이터 SpannableStringBuilder 타입으로 변환
                        val builder = SpannableStringBuilder(item.folderName)
                        // index부터 검색어 끝부분까지 색상 적용
                        val highlightColor = ForegroundColorSpan(context.resources.getColor(R.color.main_color))
                        builder.setSpan(highlightColor, index, index + searchWord.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        // index부터 검색어 끝부분까지 bold 처리
                        val boldSpan = StyleSpan(Typeface.BOLD)
                        builder.setSpan(boldSpan, index, index + searchWord.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        // 폴더 textview에 적용
                        binding.textviewLinkItemFolder.text = builder

                        // 폴더에서 검색어 위치 탐색
                        index = item.folderName.indexOf(searchWord, index + 1)
                    }
                }
            }
            // 폴더가 존재하지 않을 경우
            else {
                binding.imgLinkItemFolder.setImageResource(R.drawable.ic_folder_not_exist) // 폴더 없음 아이콘
                binding.textviewLinkItemFolder.text = "폴더 없음"                            // 폴더명 = 폴더 없음
            }

            // 썸네일 이미지가 존재할 경우
            if(item.thumbnailImage != null) {
                CoroutineScope(Dispatchers.Main).launch {
                    val thumbnailImageBitmap: Bitmap? = withContext(Dispatchers.IO) {
                        ImageLoader.loadImage(item.thumbnailImage)
                    }
                    binding.imgLinkItemThumbnail.setImageBitmap(thumbnailImageBitmap) // 썸네일 이미지
                }
            }
            // 썸네일 이미지가 존재하지 않을 경우
            else {
                binding.imgLinkItemThumbnail.setImageResource(R.drawable.img_link_item_thumbnail_default) // 썸네일 없을 경우 이미지
            }

            // 링크 출처 플랫폼을 알 수 없는 경우
            if(item.linkItemSource == null) {
                binding.imgLinkItemSource.setImageResource(R.drawable.ic_link_list_item_source_default) // 링크 모를 때 표시하는 아이콘
            }
            // 링크 출처 플랫폼이 인스타그램, 트위터, 네이버블로그 중 하나인 경우
            else {
                val iconResourceName = "ic_link_list_item_source_" + item.linkItemSource
                val iconResourceId = context.resources.getIdentifier(iconResourceName, "drawable", context.packageName)
                binding.imgLinkItemSource.setImageResource(iconResourceId)
            }

            // 링크 기록 내용에 포함된 아이콘 리스트를 보여줄 recyclerview 세팅
            binding.recyclerviewLinkItemForm.apply {
                adapter = LinkItemFormAdapter(context).build(item.linkItemForms)
                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
        }
    }
}