package com.dwgu.linkive.Home.HomeLinkListRecycler

import android.content.Context
import android.graphics.Outline
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.ItemLinkOfListBinding

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
            // 제목
            binding.textviewLinkItemTitle.text = item.linkTitle

            roundTop(binding.imgLinkItemThumbnail, 24f)
        }
    }

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