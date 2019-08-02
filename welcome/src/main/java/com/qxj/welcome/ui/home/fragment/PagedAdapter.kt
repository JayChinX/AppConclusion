package com.qxj.welcome.ui.home.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.qxj.welcome.R
import com.qxj.welcome.ui.home.data.model.OneData

class PagedAdapter : PagedListAdapter<OneData, PagedAdapter.PagedViewHolder>(diffCallback) {

    private val TAG = PagedAdapter::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagedViewHolder =
            PagedViewHolder(parent)

    override fun onBindViewHolder(holder: PagedViewHolder, position: Int) =
            holder.bindTo(getItem(position))

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<OneData>() {

            override fun areItemsTheSame(oldItem: OneData, newItem: OneData): Boolean =
                    oldItem.id == newItem.id


            override fun areContentsTheSame(oldItem: OneData, newItem: OneData): Boolean =
                    oldItem == newItem

        }
    }

    class PagedViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_one, parent, false)
    ) {
        private val nameView = itemView.findViewById<TextView>(R.id.name_one)
        private var data: OneData? = null
        fun bindTo(data: OneData?) {
            this.data = data
            nameView.text = data?.name
        }
    }
}
