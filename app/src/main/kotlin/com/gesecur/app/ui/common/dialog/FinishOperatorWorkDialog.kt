package com.gesecur.app.ui.common.dialog;

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.gesecur.app.R
import com.gesecur.app.databinding.FinishOperatorWorkDialogBinding

class FinishOperatorWorkDialog(
    context: Context,
    val callback: ((Int) -> Unit)
) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.finish_operator_work_dialog)

        configureView()
    }


    private fun configureView() {
        findViewById<Button>(R.id.btn_ok).setOnClickListener {
            onAccept()
        }
    }

    private fun onAccept() {

        with(findViewById<EditText>(R.id.edit_time).text) {
            val extra = if(isNullOrEmpty()) 0 else toString().toInt()

            callback(extra)
            dismiss()
        }

    }

    public override fun onStart() {
        super.onStart()

        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }
}