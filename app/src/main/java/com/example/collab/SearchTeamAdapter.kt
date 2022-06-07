package com.example.collab

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.collab.databinding.TeamInfoRowBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.team_info_row.view.*

class SearchTeamAdapter(val items: ArrayList<TeamData>): RecyclerView.Adapter<SearchTeamAdapter.ViewHolder>() {

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
        fun OnItemClick(position: Int)
    }
    var itemClickListener:OnItemClickListener?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TeamInfoRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(val binding: TeamInfoRowBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                itemClickListener!!.OnItemClick(bindingAdapterPosition)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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