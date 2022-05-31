package com.example.collab

import android.app.Dialog
import android.view.WindowManager
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast


class PlanDialog(context: PersonalCalendarActivity) {
    var context = this
    private val dialog = Dialog(context)
    private lateinit var onDismissedListener: onPlanCreateClickListener

    fun onDismissedListener(listener: onPlanCreateClickListener) {
        onDismissedListener = listener
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
            onDismissedListener.onPlanCreateClick("확인")
            var planTitle = dialog.findViewById<TextView>(R.id.planTitle).text.toString()
            var planStartTime = dialog.findViewById<TextView>(R.id.planStartTime).text.toString()
            var planFinishTime = dialog.findViewById<TextView>(R.id.planFinishTime).text.toString()

            if (isValidPlan(planTitle,planStartTime,planFinishTime)) {
                //TODO: 데이터베이스에 추가하기
                dialog.dismiss()
            }
        }
        dialog.findViewById<CalendarView>(R.id.calendar).setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
            var toast = String.format("%d / %d / %d", year, month + 1, dayOfMonth)
            Toast.makeText(dialog.context, toast, Toast.LENGTH_SHORT).show()
            dialog.findViewById<TextView>(R.id.planStartTime).text = year.toString()+"-"+(month+1).toString()+"-"+dayOfMonth.toString()+"/12:00"
            dialog.findViewById<TextView>(R.id.planFinishTime).text = year.toString()+"-"+(month+1).toString()+"-"+(dayOfMonth+1).toString()+"/12:00"
        }
//        dialog.setOnDismissListener {}
    }

    private fun isValidPlan(planTitle: String, planStartTime: String, planFinishTime: String): Boolean {
        //TODO: isValid 만들기
        return true;
    }

    interface onPlanCreateClickListener {
        fun onPlanCreateClick(name: String)
    }

}