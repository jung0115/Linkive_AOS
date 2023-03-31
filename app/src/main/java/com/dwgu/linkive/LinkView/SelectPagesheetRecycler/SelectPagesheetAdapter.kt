package com.dwgu.linkive.LinkView.SelectPagesheetRecycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.ItemSelectPagesheetBinding

// PageSheet 선택 BottomSheet에 나타나는 PageSheet List
class SelectPagesheetAdapter (
    private val context: Context,
    val setAllUncheck: () -> Unit) :
    RecyclerView.Adapter<SelectPagesheetAdapter.SelectPagesheetViewHolder>()  {

    var items = mutableListOf<SelectPagesheetItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) :
            SelectPagesheetViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_select_pagesheet, parent, false)

        return SelectPagesheetViewHolder(ItemSelectPagesheetBinding.bind(view))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SelectPagesheetViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class SelectPagesheetViewHolder(private val binding: ItemSelectPagesheetBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SelectPagesheetItem) {
            binding.textSelectPagesheetName.text = item.pagesheetName // 폴더명

            // 체크박스 선택 상태일 때
            if(item.selectPagesheet) {
                // 체크 완료 아이콘
                binding.btnSelectPagesheetCheckbox.setImageResource(R.drawable.ic_link_item_form_checkbox)
            }
            // 체크박스 미선택 상태일 때
            else {
                // 체크 해제 아이콘
                binding.btnSelectPagesheetCheckbox.setImageResource(R.drawable.ic_link_item_form_checkbox_unchecked)
            }

            // 체크박스 선택 시
            binding.btnSelectPagesheetCheckbox.setOnClickListener {
                // 체크박스 전체 선택 해제
                setAllUncheck()

                // 지금 선택한 체크박스만 선택
                item.selectPagesheet = true
            }
        }
    }
}