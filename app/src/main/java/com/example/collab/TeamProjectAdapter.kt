package com.example.collab

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.team_info_row.view.*

class TeamProjectAdapter(val items: ArrayList<TeamProject>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var firestore : FirebaseFirestore?= null
    var teamProject: ArrayList<TeamProject> = arrayListOf()
    var data : ArrayList<String>? =null
    init {
        firestore = FirebaseFirestore.getInstance()
        val email = "mindonghun99@naver.com"
        val test = firestore
            ?.collection("User")
            ?.document(email)
            ?.addSnapshotListener { value, error ->
                data = value?.data?.get("team") as ArrayList<String>
                Log.i("test1", data.toString())
            }

        for(teamName in data!!){

        }
        firestore?.collection("Team")
            ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                teamProject.clear()
                for (snapshot in querySnapshot!!.documents) {
                    Log.i("test", snapshot.id)
                    var item = snapshot.toObject(TeamProject::class.java)
                    teamProject.add(item!!)
                }
                notifyDataSetChanged()
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
                itemClickListener?.OnItemClick(items[adapterPosition], adapterPosition)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var viewHolder = (holder as ViewHolder).itemView
        viewHolder.teamName.text = teamProject[position].teamName
        viewHolder.teamSubject.text = teamProject[position].subject
    }

    override fun getItemCount(): Int {
        return teamProject.size
    }

//    fun searchTeam(searchWord: String) {
//        firestore?.collection("Team")
//            ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
//                teamProject.clear()
//
//                for (snapshot in querySnapshot!!.documents) {
//                    if (snapshot.toString().contains(searchWord)) {
//                        var item = snapshot.toObject(TeamProject::class.java)
//                        teamProject.add(item!!)
//                    }
//                    else{
////                        Toast.makeText(
////                            applicationContext,
////                            "검색 결과가 없습니다",
////                            Toast.LENGTH_SHORT
////                        ).show()
//                    }
//                }
//                notifyDataSetChanged()
//            }
//
//    }
}