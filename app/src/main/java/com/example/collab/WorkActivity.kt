package com.example.collab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collab.databinding.ActivityWorkBinding
import kotlinx.android.synthetic.main.activity_work.*

class WorkActivity : AppCompatActivity() {
    lateinit var binding:ActivityWorkBinding
    lateinit var adapter : WorkAdapter
    var context = this
    var todoList: ArrayList<String> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout() {
        var iteamName = intent.getStringExtra("teamName")

        adapter = WorkAdapter(todoList,iteamName!!)


        binding.apply{
            teamName.text = iteamName
        }

        teamWorkRecyclerView.adapter = adapter
        teamWorkRecyclerView.layoutManager = LinearLayoutManager(context)


    }
}