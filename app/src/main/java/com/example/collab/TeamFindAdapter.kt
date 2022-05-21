package com.example.collab

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.collab.databinding.TeamFindRowBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class TeamFindAdapter(options: FirebaseRecyclerOptions<TeamHeadInfo>)
    : FirebaseRecyclerAdapter<TeamHeadInfo, TeamFindAdapter.ViewHolder>(options)
{
    interface OnItemClickListener{
        fun OnItemClick(position:Int)
    }

    var itemClickListener:OnItemClickListener?=null

    inner class ViewHolder(val binding: TeamFindRowBinding) : RecyclerView.ViewHolder(binding.root){
        init{
            binding.root.setOnClickListener{
                itemClickListener!!.OnItemClick(bindingAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = TeamFindRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: TeamHeadInfo) {
        holder.binding.apply{
            rowTeamfindName.text = model.name
            rowTeamfindMaster.text = model.master

        }
    }


}