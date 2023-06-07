package com.dwgu.linkive.MyPage.MyPageRecycler

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.PageSheetItemBinding


class BasicPageSheetAdapter(
    private val context: Context,
    val sheetClick: () -> Unit
): RecyclerView.Adapter<BasicPageSheetAdapter.ViewHolder>() {

    var data = mutableListOf<PageSheetItem>()
    lateinit var binding: PageSheetItemBinding

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(context)
            .inflate(R.layout.page_sheet_item, parent, false)
        return ViewHolder(PageSheetItemBinding.bind(inflatedView))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            sheetClick()
            Log.d("msg", "sheetClick")
        }
    }

    inner class ViewHolder(val binding: PageSheetItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PageSheetItem) {
            binding.btnPagesheetName.text = item.name
        }
    }
}