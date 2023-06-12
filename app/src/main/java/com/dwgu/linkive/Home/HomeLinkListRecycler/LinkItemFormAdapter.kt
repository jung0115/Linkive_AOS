package com.dwgu.linkive.Home.HomeLinkListRecycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.ItemLinkItemFormBinding

class LinkItemFormAdapter(private val context: Context) :
    RecyclerView.Adapter<LinkItemFormAdapter.LinkItemFormViewHolder>() {

    var items = mutableSetOf<String>()

    fun build(itemForms: MutableSet<String>?): LinkItemFormAdapter {
        if(itemForms != null)
            items = itemForms

        return this
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) :
            LinkItemFormViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_link_item_form, parent, false)

        return LinkItemFormViewHolder(ItemLinkItemFormBinding.bind(view))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: LinkItemFormViewHolder, position: Int) {
        holder.bind(items.elementAt(position))
    }

    inner class LinkItemFormViewHolder(private val binding: ItemLinkItemFormBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            // 링크 내용에 포함된 아이템 요소 아이콘
            val iconResourceName = "ic_link_item_form_" + item
            val iconResourceId = context.resources.getIdentifier(iconResourceName, "drawable", context.packageName)
            binding.imgLinkItemForm.setImageResource(iconResourceId)
        }
    }
}