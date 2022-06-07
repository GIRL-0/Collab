package com.example.collab

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.common.collect.Maps
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.detail_work_row.view.*
import java.lang.reflect.Array

class DetailWorkAdapter (val items: ArrayList<String>,val teamName:String,val todo:String,val pcontext:DetailWorkActivity): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var firestore: FirebaseFirestore? = null
    val checkList :ArrayList<Boolean> = ArrayList()
    var count_true = 0
    var progressnum = 0
    var flag = true
    init {
        firestore = FirebaseFirestore.getInstance()
        firestore?.collection("Team")
            ?.document(teamName)
            ?.collection("info")
            ?.document("todoList")
            ?.addSnapshotListener{value2,error->
                items.clear()
                checkList.clear()
                count_true = 0
                progressnum = 0
                if(value2?.contains(todo+"_content")==true) {
                    val data = value2?.get(todo+"_content") as ArrayList<String>

                    for(str in data) {
                        items.add(str)
                        val check = value2?.get(todo+str) as Boolean
                        if(check)
                            count_true++
                        checkList.add(check)
                    }

                    progressnum = ((count_true.toDouble()/checkList.size.toDouble())*100).toInt()
                    pcontext.binding.workProgressNum.text = progressnum.toString()
                    pcontext.binding.workProgressRate.progress = progressnum
                    firestore?.collection("Team")
                        ?.document(teamName)
                        ?.collection("info")
                        ?.document("todoList")?.update(todo+"_progress",progressnum)
                    if(flag) {
                        notifyDataSetChanged()
                    }else{
                        flag = true
                    }
                }
            }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.detail_work_row, parent, false)
        return ViewHolder(view)
    }

    interface OnItemClickListener{
        fun OnItemClick(data: String , position: Int)
    }
    var itemClickListener:OnItemClickListener?=null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.detailWork.setOnClickListener {
                flag = false
                val todoList = firestore?.collection("Team")
                    ?.document(teamName)
                    ?.collection("info")
                    ?.document("todoList")

                todoList?.get()?.addOnSuccessListener {
                    if(it?.get(todo+items[absoluteAdapterPosition]) == true){
                        todoList.update(todo+items[absoluteAdapterPosition], false)
                    }else{
                        todoList.update(todo+items[absoluteAdapterPosition], true)
                    }
                }



            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var viewHolder = (holder as ViewHolder).itemView
        viewHolder.detailWork.text = items[position]
        viewHolder.detailWork.isChecked = checkList[position]
    }

    override fun getItemCount(): Int {
        return items.size
    }

}
