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
//        binding.personalPlanAddBtn.setOnClickListener {
//            Toast.makeText(applicationContext, "눌림", Toast.LENGTH_SHORT).show()
//            val dialog = CreatePlanDialog(this)
//            dialog.showDialog()
//            dialog.setOnClickListener(object : CreatePlanDialog.OnDialogClickListener {
//                override fun onClicked(name: String)
//                {
//                    Toast.makeText(applicationContext, "완료", Toast.LENGTH_SHORT).show()
//                }
//
//            })
//        }
    }

}