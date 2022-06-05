package com.example.collab

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.team_info_row.view.*

class SearchTeamAdapter(val items: ArrayList<TeamData>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var firestore : FirebaseFirestore?= null
    var teamInfo: ArrayList<TeamData> = arrayListOf()

    init {
        firestore = FirebaseFirestore.getInstance()
        firestore?.collection("Team")
            ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                teamInfo.clear()
                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject(TeamData::class.java)
                    teamInfo.add(item!!)
                }
                notifyDataSetChanged()
            }
    }

    interface OnItemClickListener{
        fun OnItemClick(data: TeamData , position: Int)
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
                itemClickListener?.OnItemClick(items[adapterPosition], adapterPosition)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var viewHolder = (holder as ViewHolder).itemView
        viewHolder.teamName.text = teamInfo[position].teamName
        viewHolder.teamSubject.text = teamInfo[position].Subject
    }

    override fun getItemCount(): Int {
        return teamInfo.size
    }

    fun searchTeam(searchWord: String) {
        firestore?.collection("Team")
            ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                teamInfo.clear()

                for (snapshot in querySnapshot!!.documents) {
                    if (snapshot.toString().contains(searchWord)) {
                        var item = snapshot.toObject(TeamData::class.java)
                        teamInfo.add(item!!)
                    }
                    else{
//                        Toast.makeText(
//                            applicationContext,
//                            "검색 결과가 없습니다",
//                            Toast.LENGTH_SHORT
//                        ).show()
                    }
                }
                notifyDataSetChanged()
            }

    }
}