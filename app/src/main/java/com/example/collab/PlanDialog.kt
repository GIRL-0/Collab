package com.example.collab

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.io.File
import java.io.FileWriter
import java.util.*


class PlanDialog(context: PersonalCalendarActivity) {
    var context = this
    private val dialog = Dialog(context)
    private lateinit var onClickListener: OnDialogClickListener

    fun setOnClickListener(listener: OnDialogClickListener) {
        onClickListener = listener
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
            onClickListener.onClicked("확인")
            var planTitle = dialog.findViewById<TextView>(R.id.planTitle).text.toString()
            var planStartTime = dialog.findViewById<TextView>(R.id.planStartTime).text.toString()
            var planFinishTime = dialog.findViewById<TextView>(R.id.planFinishTime).text.toString()
            /**
             * 데이터베이스 연결
             */
            Toast.makeText(dialog.context, "데이터베이스 연결하기", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.setOnDismissListener {

        }
    }

    interface OnDialogClickListener {
        fun onClicked(name: String)
    }

}