package com.example.collab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collab.databinding.FragmentFindTeamBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class FindTeamFragment : Fragment() {
    lateinit var layoutManager:LinearLayoutManager
    lateinit var adapter: TeamFindAdapter
    lateinit var db:DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_find_team,container,false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.findteam_recyclerview)

        layoutManager = LinearLayoutManager(view.context,LinearLayoutManager.VERTICAL,false)
        db = Firebase.database.getReference("Team")
        val query = db.limitToLast(50)
        val option = FirebaseRecyclerOptions.Builder<TeamHeadInfo>()
            .setQuery(query,TeamHeadInfo::class.java)
            .build()
        adapter = TeamFindAdapter(option)
        adapter.itemClickListener=object:TeamFindAdapter.OnItemClickListener{
            override fun OnItemClick(position: Int) {

            }
        }
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter


        return view
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}