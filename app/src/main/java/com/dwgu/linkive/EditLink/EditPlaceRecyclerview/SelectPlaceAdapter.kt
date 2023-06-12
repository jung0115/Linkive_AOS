package com.dwgu.linkive.EditLink.EditPlaceRecyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.ItemSelectPlaceBinding

// 장소 선택 BottomSheet에서 장소 리스트 RecyclerView
class SelectPlaceAdapter(private val context: Context) :
    RecyclerView.Adapter<SelectPlaceAdapter.SelectPlaceViewHolder>() {

    var items = mutableListOf<SelectPlaceItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) :
            SelectPlaceViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_select_place, parent, false)

        return SelectPlaceViewHolder(ItemSelectPlaceBinding.bind(view))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SelectPlaceViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class SelectPlaceViewHolder(private val binding: ItemSelectPlaceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SelectPlaceItem) {
            binding.textSelectPlaceName.text = item.placeName      // 장소명
            binding.textSelectPlaceAddress.text = item.loadAddress // 주소

            // 체크박스 선택 상태일 때
            if(item.selectPlace) {
                // 체크 완료 아이콘
                binding.btnSelectPlaceCheckbox.setImageResource(R.drawable.ic_link_item_form_checkbox)
            }
            // 체크박스 미선택 상태일 때
            else {
                // 체크 해제 아이콘
                binding.btnSelectPlaceCheckbox.setImageResource(R.drawable.ic_link_item_form_checkbox_unchecked)
            }

            // 체크박스 선택 시
            binding.btnSelectPlaceCheckbox.setOnClickListener {
                // 체크박스 전체 선택 해제
                setAllUncheck()

                // 지금 선택한 체크박스만 선택
                item.selectPlace = true

                // 선택 결과를 적용하기 위해 데이터 변경했다고 알려주기
                notifyDataSetChanged()
            }
        }
    }

    // 체크박스 전체 선택 해제
    fun setAllUncheck() {
        for(ckbox in items) {
            ckbox.selectPlace = false
        }
    }
}