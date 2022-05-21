package com.example.collab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.collab.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    val textarr = arrayListOf<String>("팀 찾기","테스트")
    val iconarr= arrayListOf<Int>(R.drawable.ic_launcher_foreground,R.drawable.ic_launcher_foreground)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout() {
        binding.viewpager.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(binding.tablayout,binding.viewpager){
            tab,position->
            tab.text = textarr[position]
        }.attach()
    }
}