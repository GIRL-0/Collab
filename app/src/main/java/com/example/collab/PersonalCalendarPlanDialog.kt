package com.example.collab

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.WindowManager
import android.widget.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class PersonalCalendarPlanDialog(context: PersonalCalendarActivity) {
    var context = this
    private val dialog = Dialog(context)
    private lateinit var onDismissedClickListener: OnPlanCreateClickListener

    fun onDismissedClickListener(listener: OnPlanCreateClickListener) {
        onDismissedClickListener = listener
    }

    interface OnPlanCreateClickListener {
        fun onPlanCreateClick(name: String)
    }

    fun showDialog() {
        dialog.setContentView(R.layout.activity_create_plan)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        dialog.findViewById<Button>(R.id.detailWorkAddBtn).setOnClickListener {
            onDismissedClickListener.onPlanCreateClick("확인")
            var planTitle = dialog.findViewById<TextView>(R.id.planTitle).text.toString()
            var planStartTime = dialog.findViewById<TextView>(R.id.planStartTime).text.toString()
            var planFinishTime = dialog.findViewById<TextView>(R.id.planFinishTime).text.toString()

            if (isValidPlan(planTitle,planStartTime,planFinishTime)) {
                //db 추가
                val db = Firebase.firestore
                val doc = db.collection("User").document(UserInfo.userInfoEmail)
                doc.update("plans", FieldValue.arrayUnion("$planTitle!$planStartTime!$planFinishTime"))
                dialog.dismiss()
            }else{
                Toast.makeText(dialog.context, "잘못 입력하셨습니다. 다시 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.findViewById<ImageView>(R.id.createCancelBtn).setOnClickListener {
            dialog.dismiss()
        }

        dialog.findViewById<CalendarView>(R.id.calendar).setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
            var toast = String.format("%d / %d / %d", year, month + 1, dayOfMonth)
//            Toast.makeText(dialog.context, toast, Toast.LENGTH_SHORT).show()
            dialog.findViewById<TextView>(R.id.planStartTime).text =
                year.toString()+"-"+(month+1).toString()+"-"+dayOfMonth.toString()+"/12:00"
            dialog.findViewById<TextView>(R.id.planFinishTime).text =
                year.toString()+"-"+(month+1).toString()+"-"+(dayOfMonth+1).toString()+"/12:00"
        }
//        dialog.setOnDismissListener {}
    }

    private fun isValidPlan(planTitle: String, planStartTime: String, planFinishTime: String): Boolean {
        //isValid
        return !(planTitle==""||planStartTime==""||planFinishTime=="")
    }



}