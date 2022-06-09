package com.example.collab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProfileNoticeAdapter(val items:ArrayList<ProfileNoticeData>) :RecyclerView.Adapter<ProfileNoticeAdapter.ViewHolder>(){
    interface OnItemClickListener{
        fun OnItemClick(data:ProfileNoticeData)
    }
    var itemClickListener:OnItemClickListener?=null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val alarmContent = itemView.findViewById<TextView>(R.id.noticeContent)
        init {
            alarmContent.setOnClickListener{
                itemClickListener?.OnItemClick(items[bindingAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.profile_notice_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.alarmContent.text = items[position].alarmContent

    }

    override fun getItemCount(): Int {
        return items.size
    }
}