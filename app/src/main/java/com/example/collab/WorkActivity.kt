package com.example.collab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.collab.databinding.ActivityWorkBinding

class WorkActivity : AppCompatActivity() {
    lateinit var binding:ActivityWorkBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout() {
        var i_teamName = intent.getStringExtra("teamName")

        binding.apply{
            teamName.text = i_teamName
        }
    }
}