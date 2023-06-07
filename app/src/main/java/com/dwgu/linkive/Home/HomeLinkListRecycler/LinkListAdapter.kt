package com.dwgu.linkive.Home.HomeLinkListRecycler

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dwgu.linkive.ImageLoader.ImageLoader
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.ItemLinkOfListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LinkListAdapter(
    private val context: Context,
    private val onClickLinkItem: (memoNum: Int) -> Unit
    ) :
    RecyclerView.Adapter<LinkListAdapter.LinkListViewHolder>() {

    var items = mutableListOf<LinkListItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) :
            LinkListViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_link_of_list, parent, false)

        return LinkListViewHolder(ItemLinkOfListBinding.bind(view))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: LinkListViewHolder, position: Int) {
        holder.bind(items[position])

        // 링크 아이템 클릭 시
        holder.itemView.setOnClickListener {
            onClickLinkItem(items[position].memoNum)
        }
    }

    inner class LinkListViewHolder(private val binding: ItemLinkOfListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LinkListItem) {

            binding.textviewLinkItemTitle.text = item.linkTitle // 제목

            // 폴더가 존재할 경우
            if(item.folderName != null) {
                binding.imgLinkItemFolder.setImageResource(R.drawable.ic_folder_exist) // 폴더 존재 아이콘
                binding.textviewLinkItemFolder.text = item.folderName                  // 폴더명
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