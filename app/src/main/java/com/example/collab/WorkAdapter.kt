package com.example.collab

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.team_info_row.view.*
import kotlinx.android.synthetic.main.work_row.view.*

class WorkAdapter(val items: ArrayList<String>,val teamName:String): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var firestore : FirebaseFirestore?= null
    var todoList : HashMap<String,ArrayList<String>> = HashMap()

    init {
        firestore = FirebaseFirestore.getInstance()
        firestore?.collection("Team")
            ?.document(teamName)
            ?.addSnapshotListener { value, error ->
                if(value?.contains("todoList")==true) {
                    val data = value?.get("todoList") as ArrayList<String>
                    for (tmp in data!!) {
                        items.add(tmp)
                        Log.i("test","documentPath 주기전")
                        firestore?.collection("Team")
                            ?.document(teamName)
                            ?.collection("info")
                            ?.document("todoList")
                            ?.addSnapshotListener{value2,error->
                                if(value2?.contains(tmp + "_content")==true) {
                                    val data2 = value2?.get(tmp + "_content") as ArrayList<String>
                                    Log.i("test", data2.toString())
                                    todoList.put(tmp, data2)
                                    Log.i("test", todoList.toString())
                                }
                                notifyDataSetChanged()
                            }
                    }
                }
            }


    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.work_row, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {

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

    }

    override fun getItemCount(): Int {
        return items.size
    }


}