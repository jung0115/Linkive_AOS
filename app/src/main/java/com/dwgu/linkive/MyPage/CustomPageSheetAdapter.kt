package com.dwgu.linkive.MyPage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.PageSheetItemBinding


class CustomPageSheetAdapter(
    val data: MutableList<PageSheetItem>,
    val sheetClick: (id: Int) -> Unit
): RecyclerView.Adapter<CustomPageSheetAdapter.ViewHolder>() {

    lateinit var binding: PageSheetItemBinding

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.page_sheet_item, parent, false)
        return ViewHolder(PageSheetItemBinding.bind(inflatedView))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        View.OnClickListener { it ->
            Toast.makeText(it.context, "Clicked: ${item.name}", Toast.LENGTH_SHORT).show()
        }
        holder.apply {
            binding.btnPagesheetName.text = item.name
            binding.btnPagesheetName.setBackgroundResource(R.drawable.button_outline_purple_10dp)
        }

        // recyclerview item click
        holder.itemView.setOnClickListener {
            sheetClick.invoke(data[position].id)
        }
    }

    class ViewHolder(val binding: PageSheetItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}