package com.example.collab

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.collab.databinding.TeamInfoRowBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.team_info_row.view.*

class SearchTeamAdapter(val items: ArrayList<TeamData>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var firestore : FirebaseFirestore?= null
    // var teamInfo: ArrayList<TeamData>? =null

    init {
        firestore = FirebaseFirestore.getInstance()
        firestore?.collection("Team")
            ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                items.clear()
                for (snapshot in querySnapshot!!.documents) {
                    Log.i("testt",snapshot.toString())
                    var item = snapshot.toObject(TeamData::class.java)
                    items.add(item!!)
                }
                notifyDataSetChanged()
            }
    }

    interface OnItemClickListener{
        fun OnItemClick(data: TeamData, position: Int)
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
        viewHolder.teamSubject.text = items[position].Subject
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun searchTeam(searchWord: String) {
        firestore?.collection("Team")
            ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                items.clear()
                for (snapshot in querySnapshot!!.documents) {
                    if (snapshot.toString().contains(searchWord)) {
                        var item = snapshot.toObject(TeamData::class.java)
                        items.add(item!!)
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