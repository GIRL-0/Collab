package com.example.collab

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.collab.databinding.TeamInfoRowBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class TeamProjectAdapter (options: FirebaseRecyclerOptions<TeamProject>)
    : FirebaseRecyclerAdapter<TeamProject, TeamProjectAdapter.ViewHolder>(options)
{
    interface OnItemClickListener{
        fun OnItemClick(position:Int)
    }

    var itemClickListener:OnItemClickListener?=null

    inner class ViewHolder(val binding: TeamInfoRowBinding) : RecyclerView.ViewHolder(binding.root){
        init{
            binding.root.setOnClickListener{
                itemClickListener!!.OnItemClick(bindingAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = TeamInfoRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: TeamProject) {
        holder.binding.apply{
            teamName.text = model.teamName
            teamSubject.text = model.teamSubject
        }
    }


}