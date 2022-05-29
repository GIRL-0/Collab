package com.example.collab

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.collab.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initCal()
    }

    private fun initCal() {
        with(binding){
            calButton.setOnClickListener{
                Toast.makeText(this@MainActivity,"눌림.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@MainActivity, PersonalCalendarActivity::class.java)
                startActivity(intent)
            }
        }
    }
}