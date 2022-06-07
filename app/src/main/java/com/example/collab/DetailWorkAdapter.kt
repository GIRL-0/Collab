package com.example.collab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.detail_work_row.view.*

class DetailWorkAdapter (val items: ArrayList<String>,val teamName:String,val todo:String): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var firestore: FirebaseFirestore? = null

    init {
        firestore = FirebaseFirestore.getInstance()
        firestore?.collection("Team")
            ?.document(teamName)
            ?.collection("info")
            ?.document("todoList")
            ?.addSnapshotListener{value2,error->
                if(value2?.contains(todo + "_content")==true) {
                    val data2 = value2?.get(todo + "_content") as ArrayList<String>
                    for(tmp in data2!!)
                        items.add(tmp)
                }
                notifyDataSetChanged()
            }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.detail_work_row, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var viewHolder = (holder as ViewHolder).itemView
        viewHolder.detailWork.text = items[position]

    }

    override fun getItemCount(): Int {
        return items.size
    }

}
