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
import kotlinx.android.synthetic.main.name_card_row.view.*
import kotlinx.android.synthetic.main.team_info_row.view.*

class TeamInfoAdapter(val items: ArrayList<UserData>,val name:String): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var firestore : FirebaseFirestore?= null
    var data : ArrayList<String>? = null

    init {
        firestore = FirebaseFirestore.getInstance()
        //val teamName = "ABC"
        firestore?.collection("Team")
            ?.document(name)
            ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                data = querySnapshot?.get("member") as ArrayList<String>
                firestore?.collection("User")
                    ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                        items.clear()
                        for (snapshot in querySnapshot!!.documents) {
                            for (member in data!!) {
                                if (snapshot.toString().contains(member)) {
                                    var item = snapshot.toObject(UserData::class.java)
                                    items.add(item!!)
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
            LayoutInflater.from(parent.context).inflate(R.layout.name_card_row, parent, false)
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
        viewHolder.userName.text = items[position].name.toString()
        viewHolder.userMajorTag.text = items[position].field.toString()
        viewHolder.userIntroduce.text = items[position].introduction.toString()
        viewHolder.userGradeStar.text = items[position].rating.toString()
        viewHolder.userGradeNum.text = items[position].rating.toString()




    }

    override fun getItemCount(): Int {
        return items.size
    }

}