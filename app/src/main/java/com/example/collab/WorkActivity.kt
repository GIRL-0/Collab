package com.example.collab

import android.content.Intent
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
        adapter.itemClickListener = object:WorkAdapter.OnItemClickListener{
            override fun OnItemClick(data: String, position: Int) {
                Intent(this@WorkActivity,DetailWorkActivity::class.java).apply{
                    putExtra("todo", data)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run{startActivity(this)}
            }
        }

        binding.apply{
            teamName.text = iteamName
        }

        teamWorkRecyclerView.adapter = adapter
        teamWorkRecyclerView.layoutManager = LinearLayoutManager(context)


    }
}