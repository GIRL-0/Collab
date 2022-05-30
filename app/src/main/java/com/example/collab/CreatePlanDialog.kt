package com.example.collab

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.widget.EditText

class CreatePlanDialog(context: PersonalCalendarActivity){
    private val dialog = Dialog(context)
    private lateinit var onClickListener: OnDialogClickListener

    fun setOnClickListener(listener: OnDialogClickListener)
    {
        onClickListener = listener
    }

    fun showDialog()
    {
        dialog.setContentView(R.layout.activity_create_plan)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        val edit_name = dialog.findViewById<EditText>(R.id.planTitle)

//        dialog.binding.setOnClickListener {
//            dialog.dismiss()
//        }
//
//        dialog.finish_button.setOnClickListener {
//            onClickListener.onClicked(edit_name.text.toString())
//            dialog.dismiss()
//        }

    }

    interface OnDialogClickListener
    {
        fun onClicked(name: String)
    }

}