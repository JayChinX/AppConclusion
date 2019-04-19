package com.qxj.welcome.data

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.qxj.welcome.R
import com.qxj.welcome.viewmodels.Data
import com.qxj.welcome.viewmodels.OneData

class PagedAdapter : PagedListAdapter<Data, PagedAdapter.PagedViewHolder>(DataDiffCallback<Data>()) {

    private val TAG = PagedAdapter::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagedViewHolder =
            PagedViewHolder(parent)

    override fun onBindViewHolder(holder: PagedViewHolder, position: Int) =
            holder.bindTo(getItem(position))


    private class DataDiffCallback<T : Data> : DiffUtil.ItemCallback<T>() {
        private val TAG = DataDiffCallback::class.java.simpleName

        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            Log.d(TAG, "")
            return oldItem.id == newItem.id
        }


        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
                oldItem == newItem

    }

    class PagedViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_one, parent, false)
    ) {
        private val nameView = itemView.findViewById<TextView>(R.id.name_one)
        private var data: OneData? = null
        fun bindTo(data: Data?) {
            this.data = data as OneData?
            nameView.text = data?.name
        }
    }
}
