package com.example.collab

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.collab.UserInfo.userInfoEmail
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.team_info_row.view.*

class TeamProjectAdapter(val items: ArrayList<TeamProject>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var firestore : FirebaseFirestore?= null
    var data : ArrayList<String>? =null
    init {
        firestore = FirebaseFirestore.getInstance()
        val email = "testemail"
        Log.i("email", email)
        firestore?.collection("User")
            ?.document(email)
            ?.addSnapshotListener { value, error ->
                items.clear()
                if(value?.contains("teams")==true)
                    data = value?.get("teams") as ArrayList<String>
                    for (team in data!!) {
                        firestore?.collection("Team")?.document(team)
                            ?.addSnapshotListener { value2, error ->
                                var item = value2?.toObject(TeamProject::class.java)
                                items.add(item!!)
                                notifyDataSetChanged()
                            }
                    }
            }

    }

    interface OnItemClickListener{
        fun OnItemClick(data: TeamProject , position: Int)
    }
    var itemClickListener:OnItemClickListener?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.team_info_row, parent, false)
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
        viewHolder.teamName.text = items[position].teamName
        viewHolder.teamSubject.text = items[position].subject
    }

    override fun getItemCount(): Int {
        return items.size
    }

}