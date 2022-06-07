package com.example.collab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.collab.databinding.ActivityCreatePlanBinding
import com.example.collab.databinding.ActivityPersonalCalendarBinding

class CreatePlanActivity : AppCompatActivity() {
    lateinit var binding: ActivityPersonalCalendarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {

    }

}