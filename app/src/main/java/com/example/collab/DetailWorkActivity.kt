package com.example.collab

import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collab.databinding.ActivityDetailWorkBinding
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_work.*

class DetailWorkActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailWorkBinding
    lateinit var adapter : DetailWorkAdapter
    var context = this
    var todoList: ArrayList<String> = arrayListOf()
    var firestore : FirebaseFirestore?= null
    var itodo:String = ""
    var iteamName:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailWorkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout() {
        itodo = intent.getStringExtra("todo")!!
        iteamName = intent.getStringExtra("teamName")!!

        adapter = DetailWorkAdapter(todoList,iteamName,itodo,context)

        binding.apply{
            workTitle.text = itodo

            detailWorkRecyclerView.adapter = adapter
            detailWorkRecyclerView.layoutManager = LinearLayoutManager(context)

            detailWorkAddBtn.setOnClickListener {
                detailAddDlg()
            }

            //setProgress()

        }
    }




    fun detailAddDlg(){
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.detail_work_add_dialog)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()
        dialog.findViewById<Button>(R.id.detailWorkAddBtn).setOnClickListener{
            var contentStr = dialog.findViewById<EditText>(R.id.detailWorkContentEdit).text.toString()

            firestore = FirebaseFirestore.getInstance()
            firestore?.collection("Team")
                ?.document(iteamName)
                ?.collection("info")
                ?.document("todoList")
                ?.get()?.addOnSuccessListener {
                    if(it?.contains(itodo+"_content")==true){

                        firestore?.collection("Team")
                            ?.document(iteamName)
                            ?.collection("info")
                            ?.document("todoList")?.update(itodo+contentStr,false)

                        firestore?.collection("Team")
                            ?.document(iteamName)
                            ?.collection("info")
                            ?.document("todoList")?.update(itodo+"_content",FieldValue.arrayUnion(contentStr))

                        firestore?.collection("Team")
                                ?.document(iteamName)
                                ?.collection("info")
                                ?.document("todoList")?.get()?.addOnSuccessListener {
                                    if(it.contains(itodo+"_progress")==false){
                                        firestore?.collection("Team")
                                            ?.document(iteamName)
                                            ?.collection("info")
                                            ?.document("todoList")?.update(itodo+"_progress",0)
                                    }
                            }

                    }
                    else{

                        val docData = hashMapOf(
                            itodo+"_content" to arrayListOf(contentStr),
                            itodo+contentStr to false,
                            itodo+"_progress" to 0
                            )

                        firestore?.collection("Team")
                            ?.document(iteamName)
                            ?.collection("info")
                            ?.document("todoList")?.set(docData,SetOptions.merge())




                    }
                }
            dialog.dismiss()
        }


    }

}
