package com.dwgu.linkive.Folder.SortFolder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.dwgu.linkive.databinding.ItemSortFolderDropdownBinding
import com.dwgu.linkive.databinding.ItemSortFolderNormalBinding


class SortFolderAdapter(context: Context, var data: List<String>) : BaseAdapter() {


    //    var context: Context? = null
//    var data: List<String>? = null
    var inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

//    fun sortFolderAdapter(context: Context, data: List<String>) {
//        this.context = context
//        this.data = data
//        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//    }

    override fun getCount(): Int {
        return if (data != null) data.size;
        else 0;
    }

    override fun getItem(position: Int): Any {
        return data[position];
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        if (convertView == null) {
//            convertView = inflater.inflate(R.layout.simple_spinner_dropdown_item, parent, false)
//        }
        val binding = ItemSortFolderNormalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val model = data[position]
        try {
            binding.spinnerText.text = model

        }catch (e: Exception) {
            e.printStackTrace()
        }
        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemSortFolderDropdownBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val model = data[position]
        try {
            binding.spinnerText.text = model

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return binding.root
    }

}