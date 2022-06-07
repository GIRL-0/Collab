package com.example.collab

import PersonalCalendarAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collab.databinding.ActivityCreatePlanBinding
import com.example.collab.databinding.ActivityPersonalCalendarBinding
import com.example.collab.databinding.ActivityTeamCalendarBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList

class CreatePlanActivity : AppCompatActivity() {

    lateinit var binding: ActivityCreatePlanBinding
    var firestore : FirebaseFirestore?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }


}