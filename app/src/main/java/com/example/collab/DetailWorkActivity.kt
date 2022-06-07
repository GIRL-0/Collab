package com.example.collab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collab.databinding.ActivityDetailWorkBinding
import com.example.collab.databinding.DetailWorkRowBinding
import kotlinx.android.synthetic.main.activity_work.*

class DetailWorkActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailWorkBinding
    lateinit var adapter : DetailWorkAdapter
    var context = this
    var todoList: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailWorkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout() {
        val itodo = intent.getStringExtra("todo")
        val iteamName = intent.getStringExtra("teamName")

        adapter = DetailWorkAdapter(todoList,iteamName!!,itodo!!)

        binding.apply{
            workTitle.text = itodo

            detailWorkRecyclerView.adapter = adapter
            detailWorkRecyclerView.layoutManager = LinearLayoutManager(context)

        }
    }
}