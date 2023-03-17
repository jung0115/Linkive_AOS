package com.dwgu.linkive.Folder.FolderListAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.ItemFolderOfListBinding

class FolderListAdapter(private val List: List<FolderListItem>): RecyclerView.Adapter<FolderListAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemFolderOfListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_folder_of_list, viewGroup, false)
        return ViewHolder(ItemFolderOfListBinding.bind(view))
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {//view->viewholder로
        viewHolder.binding.textviewFolderName.text = List[position].name
    }

    //동일
    override fun getItemCount() = List.size

}