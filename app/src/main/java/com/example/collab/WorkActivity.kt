package com.example.collab

import android.app.Dialog
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_HISTORY
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collab.databinding.ActivityWorkBinding
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_work.*

class WorkActivity : AppCompatActivity() {
    lateinit var binding:ActivityWorkBinding
    lateinit var adapter : WorkAdapter
    var context = this
    var todoList: ArrayList<String> = arrayListOf()
    var firestore : FirebaseFirestore?= null
    var iteamName:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout() {
        iteamName = intent.getStringExtra("teamName") as String

        adapter = WorkAdapter(todoList,iteamName!!)
        adapter.itemClickListener = object:WorkAdapter.OnItemClickListener{
            override fun OnItemClick(data: String, position: Int) {
                Intent(this@WorkActivity,DetailWorkActivity::class.java).apply{
                    putExtra("teamName", iteamName)
                    putExtra("todo", data)
                    putExtra("workNum",position+1)
                    addFlags(FLAG_ACTIVITY_NO_HISTORY)
                }.run{startActivity(this)}
            }
        }

        binding.apply{
            teamName.text = iteamName
            teamWorkRecyclerView.adapter = adapter
            teamWorkRecyclerView.layoutManager = LinearLayoutManager(context)
            workAddBtn.setOnClickListener {
                workAddDlg()
            }



        }
    }

    fun workAddDlg() {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.work_add_dialog)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()
        dialog.findViewById<Button>(R.id.workAddBtn).setOnClickListener{
            val str = dialog.findViewById<EditText>(R.id.workTitleEdit).text.toString()

            firestore = FirebaseFirestore.getInstance()
            firestore?.collection("Team")
                ?.document(iteamName)
                ?.get()?.addOnSuccessListener {
                    if(it?.contains("todoList")==true){
                        firestore?.collection("Team")
                            ?.document(iteamName)
                            ?.update("todoList", FieldValue.arrayUnion(str))
                    }
                    else{
                        val docData = hashMapOf(
                            "todoList" to arrayListOf(str)
                        )
                        firestore?.collection("Team")
                            ?.document(iteamName)
                            ?.set(docData, SetOptions.merge())
                    }
                }
            dialog.dismiss()
        }
    }

}