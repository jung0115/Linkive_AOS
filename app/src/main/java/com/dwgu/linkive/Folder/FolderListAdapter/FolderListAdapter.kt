package com.dwgu.linkive.Folder.FolderListAdapter

import android.graphics.Outline
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.dwgu.linkive.Folder.FolderApi.ReadFoldersResponse
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.ItemFolderOfListBinding

class FolderListAdapter(private val List: ArrayList<ReadFoldersResponse>): RecyclerView.Adapter<FolderListAdapter.ViewHolder>() {

    //커스텀 리스너
    interface OnItemClickListner{
        fun onItemClick(view: View, position: Int)
    }

    //객체 저장 변수
    private lateinit var itemClickListner: OnItemClickListner

    //객체 전달 메서드
    fun setOnItemclickListner(onItemClickListner: OnItemClickListner){
        itemClickListner = onItemClickListner
    }

    inner class ViewHolder(val binding: ItemFolderOfListBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION && itemClickListner != null){
                    itemClickListner.onItemClick(binding.root, pos)
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_folder_of_list, viewGroup, false)
        return ViewHolder(ItemFolderOfListBinding.bind(view))
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {//view->viewholder로

        viewHolder.binding.textviewFolderName.text = List[position].name

        // 썸네일 지정
//        viewHolder.binding.imgFolderCover.setImageResource(List[position].thumbnail)
        roundTop(viewHolder.binding.imgFolderCover, 24f)

//        // 클릭 이벤트
//        viewHolder.binding.root.setOnClickListener {
//
////            parentFragmentManager.beginTransaction()
////                .replace(R.id.nav_host_fragment, folderFragment)
////                .commit()
//
//        }
    }

    //동일
    override fun getItemCount() = List.size


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