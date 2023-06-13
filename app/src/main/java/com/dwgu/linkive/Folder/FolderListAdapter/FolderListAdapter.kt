package com.dwgu.linkive.Folder.FolderListAdapter

import android.content.Context
import android.graphics.Outline
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.dwgu.linkive.Folder.FolderApi.ReadFoldersList
//import com.dwgu.linkive.Folder.RemoveFolderListener
//import com.dwgu.linkive.Folder.FolderApi.ReadFoldersResponse
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.ItemFolderOfListBinding

class FolderListAdapter(private val context: Context, private val List: ArrayList<ReadFoldersList.ReadFoldersResponse>, private val mode: Int = 0): RecyclerView.Adapter<FolderListAdapter.ViewHolder>(){

    //커스텀 리스너
    interface OnItemClickListner{
        fun onItemClick(view: View, position: Int, mode: Int, folder: ReadFoldersList.ReadFoldersResponse)
    }

    //아이템 클릭 리스너
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
                    itemClickListner.onItemClick(binding.root, pos, mode, List[pos])
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

        if(List[position].memoCount == null){
            viewHolder.binding.textviewCountLink.text = "0"
        }
        else {
            viewHolder.binding.textviewCountLink.text = List[position].memoCount.toString()
        }



        val thumbnailName = List[position].thumbnail.split(".")[0] // 확장자 제외한 파일 이름 추출
        val resourceId = context.resources.getIdentifier(thumbnailName, "drawable", context.packageName)
        Log.d("bgcolor", resourceId.toString())
        if (resourceId != 0) {
            viewHolder.binding.imgFolderCover.setImageResource(resourceId)
        } else {
            // 리소스를 찾을 수 없는 경우 대체 작업 수행
        }
        viewHolder.binding.imgFolderCover.setImageResource(resourceId)
        if (List[position].isLocked){
            viewHolder.binding.imgLockFolder.visibility = View.VISIBLE
        }

        if (mode == 1){
            viewHolder.binding.imgRemoveFolder.visibility = View.VISIBLE
        }
        else {
            viewHolder.binding.imgRemoveFolder.visibility = View.GONE
        }

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