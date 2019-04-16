package com.qxj.multichannel.paging

import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.qxj.commondata.R

class StudentViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.student_item, parent, false)) {
    private val nameView = itemView.findViewById<TextView>(R.id.name)
    var student: Student? = null
    fun bindTo(student: Student?) {
        this.student = student
        nameView.text = student?.name
    }
}

class StudentAdapter : PagedListAdapter<Student, StudentViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder =
            StudentViewHolder(parent)


    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    companion object {
        /**
         * 判断两个item数据是否相等
         */
        private val diffCallback = object : DiffUtil.ItemCallback<Student>() {

            override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean =
                    oldItem.id == newItem.id


            override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean =
                    oldItem == newItem


        }
    }
}