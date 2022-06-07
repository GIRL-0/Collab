package com.example.collab

import android.content.Intent
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.team_info_row.view.*
import kotlinx.android.synthetic.main.work_row.view.*

class WorkAdapter(val items: ArrayList<String>,val teamName:String): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var firestore : FirebaseFirestore?= null
    var todoList : HashMap<String,ArrayList<String>> = HashMap()
    var progressMap : HashMap<String,Int> = HashMap()

    init {
        firestore = FirebaseFirestore.getInstance()
        firestore?.collection("Team")
            ?.document(teamName)
            ?.addSnapshotListener { value, error ->
                items.clear()
                todoList.clear()
                progressMap.clear()
                if(value?.contains("todoList")==true) {
                    val data = value?.get("todoList") as ArrayList<String>
                    for (tmp in data!!) {
                        items.add(tmp)
                        firestore?.collection("Team")
                            ?.document(teamName)
                            ?.collection("info")
                            ?.document("todoList")
                            ?.addSnapshotListener{value2,error->
                                if(value2?.exists()==true) {
                                    if (value2?.contains(tmp + "_content") == true) {
                                        val data2 =
                                            value2?.get(tmp + "_content") as ArrayList<String>
                                        todoList.put(tmp, data2)
                                    }
                                    if (value2?.contains(tmp + "_progress") == true) {
                                        Log.i("testV3", "test")
                                        val data3 = value2?.get(tmp + "_progress") as Number
                                        progressMap.put(tmp, data3.toInt())
                                    }
                                }
                                notifyDataSetChanged()
                            }
                    }
                }
            }


    }

    interface OnItemClickListener{
        fun OnItemClick(data: String , position: Int)
    }
    var itemClickListener:OnItemClickListener?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.work_row, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.workItem.setOnClickListener {
                if(view.detailWorkContents.visibility == LinearLayout.VISIBLE) {
                    view.detailWorkContents.visibility = LinearLayout.GONE
                }else if(view.detailWorkContents.visibility == LinearLayout.GONE){
                    view.detailWorkContents.visibility = LinearLayout.VISIBLE
                }
            }
            view.detailBtn.setOnClickListener {
                itemClickListener?.OnItemClick(items[absoluteAdapterPosition], absoluteAdapterPosition)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var viewHolder = (holder as ViewHolder).itemView
        val todo = items[position]
        viewHolder.workTitle.text = todo
        if(todoList[todo] != null){
            val strList = todoList[todo]
            var str:String =""
            for(tmp in strList!!){
                str += tmp+"\n"
            }
            viewHolder.detailWorkListText.text = str
        }
        if(progressMap[todo] != null){
            val progress = progressMap[todo]!!
            viewHolder.workProgressRate.progress = progress
            viewHolder.workProgressNum.text = progress.toString()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }


}