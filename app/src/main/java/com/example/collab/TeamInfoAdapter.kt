package com.example.collab

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.collab.UserInfo.userInfoEmail
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.activity_team_info.*
import kotlinx.android.synthetic.main.manage_team_row.view.*
import kotlinx.android.synthetic.main.team_info_row.view.*

class TeamInfoAdapter(val items: ArrayList<UserData>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var firestore : FirebaseFirestore?= null
    var data : ArrayList<String>? = null

    init {
        firestore = FirebaseFirestore.getInstance()
        val teamName = "Collab"
        firestore?.collection("Team")
            ?.document(teamName)
            ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                data = querySnapshot?.get("member") as ArrayList<String>
                Log.i("testhyeseon", data.toString())
                firestore?.collection("User")
                    ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                        items.clear()

                        for (snapshot in querySnapshot!!.documents) {
                            for (member in data!!) {
                                Log.i("test4",member)
                                Log.i("test2", snapshot.toString())
                                if (snapshot.toString().contains(member)) {
                                    var item = snapshot.toObject(UserData::class.java)
                                    Log.i("test5", item.toString())
                                    items.add(item!!)
                                    Log.i("test3", items.toString())
                                }
                            }
                        }
                        notifyDataSetChanged()
                    }
            }
    }

    interface OnItemClickListener{
        fun OnItemClick(data: UserData, position: Int)
    }
    var itemClickListener:OnItemClickListener?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.manage_team_row, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {
                itemClickListener?.OnItemClick(items[absoluteAdapterPosition], absoluteAdapterPosition)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var viewHolder = (holder as ViewHolder).itemView
        viewHolder.memberName.text = items[position].userName.toString()

    }

    override fun getItemCount(): Int {
        return items.size
    }

}