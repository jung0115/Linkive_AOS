package com.dwgu.linkive.Home.HomeLinkListRecycler

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Outline
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.dwgu.linkive.ImageLoader.ImageLoader
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.ItemLinkOfListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LinkListAdapter(private val context: Context) :
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
        /*holder.itemView.setOnClickListener {

        }*/
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

            // 썸네일 이미지 상단 꼭짓점 radius
            roundTop(binding.imgLinkItemThumbnail, 24f)
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
            // 링크 출처 플랫폼이 인스타그램인 경우
            else if(item.linkItemSource == "instagram") {
                binding.imgLinkItemSource.setImageResource(R.drawable.ic_link_list_item_source_instagram)
            }
            // 링크 출처 플랫폼이 트위터인 경우
            else if(item.linkItemSource == "twitter") {
                binding.imgLinkItemSource.setImageResource(R.drawable.ic_link_list_item_source_twitter)
            }
            // 링크 출처 플랫폼이 네이버블로그인 경우
            else if(item.linkItemSource == "naver_blog") {
                binding.imgLinkItemSource.setImageResource(R.drawable.ic_link_list_item_source_naver_blog)
            }
        }
    }

    // 썸네일 이미지 상단 꼭짓점 둥글게
    fun roundTop(iv: ImageView, curveRadius : Float)  : ImageView {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            iv.outlineProvider = object : ViewOutlineProvider() {

                @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                override fun getOutline(view: View?, outline: Outline?) {
                    outline?.setRoundRect(0, 0, view!!.width, (view.height + curveRadius).toInt(), curveRadius)
                }
            }

            iv.clipToOutline = true
        }
        return iv
    }
}