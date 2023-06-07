package com.dwgu.linkive.MyPage.PageSheetRecycler

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dwgu.linkive.MyPage.MyPageRecycler.PageSheetItem
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.EmptyLayoutBinding
import com.dwgu.linkive.databinding.FragmentPageSheetBinding
import java.security.acl.LastOwnerException

class PageSheetAdapter(
    private val context: Context
//    val sheetClick: () -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data = mutableListOf<PageSheetContentItem>()
    lateinit var binding: FragmentPageSheetBinding

    override fun getItemViewType(position: Int): Int {
        return data[position].type
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View?
        return when(viewType) {
            text -> {
                Log.d("msg", "textview work")
                view = LayoutInflater.from(context).inflate(
                    R.layout.text_layout, parent, false
                )
                ViewHolder(view)
            }
            image -> {
                view = LayoutInflater.from(context).inflate(
                    R.layout.image_layout, parent, false
                )
                ViewHolder(view)
            }
            link -> {
                view = LayoutInflater.from(context).inflate(
                    R.layout.link_layout, parent, false
                )
                ViewHolder(view)
            }
            location -> {
                view = LayoutInflater.from(context).inflate(
                    R.layout.location_layout, parent, false
                )
                ViewHolder(view)
            }
            check -> {
                view = LayoutInflater.from(context).inflate(
                    R.layout.checkbox_layout, parent, false
                )
                ViewHolder(view)
            }
            else -> {
                view = LayoutInflater.from(context).inflate(
                    R.layout.code_layout, parent, false
                )
                ViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(data[position].type) {
            1 -> {
                (holder as ViewHolder).bind(data[position])
                holder.setIsRecyclable(false)
            }
            image -> {
                (holder as ViewHolder).bind(data[position])
                holder.setIsRecyclable(false)
            }
            link -> {
                (holder as ViewHolder).bind(data[position])
                holder.setIsRecyclable(false)
            }
            location -> {
                (holder as ViewHolder).bind(data[position])
                holder.setIsRecyclable(false)
            }
            check -> {
                (holder as ViewHolder).bind(data[position])
                holder.setIsRecyclable(false)
            }
            code -> {
                (holder as ViewHolder).bind(data[position])
                holder.setIsRecyclable(false)
            }
        }
//        holder.itemView.setOnClickListener {
//            sheetClick()
//        }
    }

//    inner class TextViewHolder(view: View): RecyclerView.ViewHolder(view) {
//        fun bind(item: PageSheetContentItem)  {
//
//        }
//    }
//
//    inner class ImageViewHolder(view: View): RecyclerView.ViewHolder(view) {
//        fun bind(item: PageSheetContentItem)  {
//
//        }
//    }
//
//    inner class LinkViewHolder(view: View): RecyclerView.ViewHolder(view) {
//        fun bind(item: PageSheetContentItem)  {
//
//        }
//    }
//
//    inner class CheckViewHolder(view: View): RecyclerView.ViewHolder(view) {
//        fun bind(item: PageSheetContentItem)  {
//
//        }
//    }
//
//    inner class CodeViewHolder(view: View): RecyclerView.ViewHolder(view) {
//        fun bind(item: PageSheetContentItem)  {
//
//        }
//    }
//
//    inner class LocationViewHolder(view: View): RecyclerView.ViewHolder(view) {
//        fun bind(item: PageSheetContentItem)  {
//
//        }
//    }

//    inner class ViewHolder(val binding: EmptyLayoutBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//            fun bind(item: PageSheetContentItem)  {
//
//            }
//    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: PageSheetContentItem) {

        }
    }
}