package com.dwgu.linkive.LinkView.MoveFolderRecycler

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.ItemMoveFolderBinding

// 폴더 이동 BottomSheet에서 폴더명 RecyclerView
class MoveFolderAdapter(private val context: Context) :
    RecyclerView.Adapter<MoveFolderAdapter.MoveFolderViewHolder>()  {

    var items = mutableListOf<MoveFolderItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) :
            MoveFolderViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_move_folder, parent, false)

        return MoveFolderViewHolder(ItemMoveFolderBinding.bind(view))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MoveFolderViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class MoveFolderViewHolder(private val binding: ItemMoveFolderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MoveFolderItem) {
            binding.textMoveFolderName.text = item.folderName // 폴더명

            // 체크박스 선택 상태일 때
            if(item.selectFolder) {
                // 체크 완료 아이콘
                binding.btnMoveFolderCheckbox.setImageResource(R.drawable.ic_link_item_form_checkbox)
            }
            // 체크박스 미선택 상태일 때
            else {
                // 체크 해제 아이콘
                binding.btnMoveFolderCheckbox.setImageResource(R.drawable.ic_link_item_form_checkbox_unchecked)
            }

            // 체크박스 선택 시
            binding.btnMoveFolderCheckbox.setOnClickListener {
                // 체크박스 전체 선택 해제
                setAllUncheck()

                // 지금 선택한 체크박스만 선택
                item.selectFolder = true

                // 선택 결과를 적용하기 위해 데이터 변경했다고 알려주기
                notifyDataSetChanged()
            }
        }
    }

    // 체크박스 전체 선택 해제
    fun setAllUncheck() {
        for(ckbox in items) {
            ckbox.selectFolder = false
        }
    }
}